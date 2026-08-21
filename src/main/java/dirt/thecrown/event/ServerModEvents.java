//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.event;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Transformation;
import dirt.thecrown.TheCrown;
import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ModItems;
import dirt.thecrown.saveddata.SavedBedBombData;
import dirt.thecrown.saveddata.SavedCrownPedestalData;
import dirt.thecrown.saveddata.SavedRecentKingData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.SuffixNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.Waypoint;
import net.minecraft.world.waypoints.WaypointStyleAsset;
import net.minecraft.world.waypoints.WaypointStyleAssets;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@SuppressWarnings("ALL")
public class ServerModEvents implements DedicatedServerModInitializer {
    static SuffixNode overworldSuffix;
    static SuffixNode netherSuffix;
    static SuffixNode endSuffix;
    CrownMobEffectUtil[] possibleCrownEffects;
    static ArrayList<UUID> loseCrownLaterUUIDs = new ArrayList<>();
    static ArrayList<UUID> removeCrownNextTickUUIDs = new ArrayList<>();

    public ServerModEvents() {
        this.possibleCrownEffects = new CrownMobEffectUtil[]{new CrownMobEffectUtil(MobEffects.BLINDNESS, 1), new CrownMobEffectUtil(MobEffects.SLOWNESS, 4), new CrownMobEffectUtil(MobEffects.POISON, 2), new CrownMobEffectUtil(MobEffects.WEAKNESS, 2), new CrownMobEffectUtil(MobEffects.HUNGER, 5), new CrownMobEffectUtil(MobEffects.MINING_FATIGUE, 3), new CrownMobEffectUtil(MobEffects.SLOW_FALLING, 1), new CrownMobEffectUtil(MobEffects.JUMP_BOOST, 2), new CrownMobEffectUtil(MobEffects.INVISIBILITY, 1), new CrownMobEffectUtil(MobEffects.SLOWNESS, 2, MobEffects.RESISTANCE, 1), new CrownMobEffectUtil(MobEffects.STRENGTH, 2), new CrownMobEffectUtil(MobEffects.HASTE, 2), new CrownMobEffectUtil(MobEffects.REGENERATION, 2), new CrownMobEffectUtil(MobEffects.RESISTANCE, 2), new CrownMobEffectUtil(MobEffects.SATURATION, 1), new CrownMobEffectUtil(MobEffects.ABSORPTION, 3)};
    }


    private static void giveCrownIcon(ServerLevel level, WaypointTransmitter waypoint) {
        ResourceKey<WaypointStyleAsset> style = ResourceKey.create(WaypointStyleAssets.ROOT_ID, Identifier.fromNamespaceAndPath("the-crown", "crown"));
        level.getWaypointManager().untrackWaypoint(waypoint);
        Consumer<Waypoint.Icon> iconConsumer = (icon) -> {
            icon.style = style;
            icon.color = Optional.of(16777215);
        };
        iconConsumer.accept(waypoint.waypointIcon());
        level.getWaypointManager().trackWaypoint(waypoint);
    }

    private static void resetWaypointIcon(ServerLevel level, ServerPlayer player) {
        level.getWaypointManager().untrackWaypoint(player);
        Consumer<Waypoint.Icon> iconConsumer = (icon) -> {
            icon.style = WaypointStyleAssets.DEFAULT;
            icon.color = Optional.empty();
        };
        iconConsumer.accept(player.waypointIcon());
        level.getWaypointManager().trackWaypoint(player);
    }

    private static int moveCrownPosCommand(CommandContext<CommandSourceStack> context) {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        Vec3 scale = Vec3Argument.getVec3(context, "scale");
        if (scale.x != scale.z) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Failed to scale crown: X and Z scales must be the same."));
            return 0;
        } else {
            SavedCrownPedestalData savedCrownPedestalData = SavedCrownPedestalData.getSavedCrownPedestalData(((CommandSourceStack)context.getSource()).getServer());
            savedCrownPedestalData.setCrownArgs(pos, scale);
            ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Moved & scaled crown pedestal." + String.valueOf(pos)).withStyle(ChatFormatting.GOLD), false);
            return 1;
        }
    }

    private static void afterPlayerChangeLevel(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
        if (TheCrown.hasLuckPerms && ModItems.isWearingCrown(player)) {
            LuckPerms lp = LuckPermsProvider.get();
            Group crownGroup = lp.getGroupManager().getGroup("crown");
            if (crownGroup == null) {
                try {
                    crownGroup = (Group)lp.getGroupManager().createAndLoadGroup("crown").get();
                    lp.getGroupManager().saveGroup(crownGroup);
                    TheCrown.LOGGER.info("Successfully created crown group");
                } catch (Exception e) {
                    TheCrown.LOGGER.error("LuckPerms group error:", e);
                }
            }

            if (crownGroup != null) {
                NodeMap crownGroupData = crownGroup.data();
                ResourceKey<Level> dim = destination.dimension();
                crownGroupData.clear();
                if (dim.equals(Level.OVERWORLD)) {
                    crownGroupData.add(overworldSuffix);
                } else if (dim.equals(Level.NETHER)) {
                    crownGroupData.add(netherSuffix);
                } else if (dim.equals(Level.END)) {
                    crownGroupData.add(endSuffix);
                }

                lp.getGroupManager().saveGroup(crownGroup);
                User currentUser = lp.getPlayerAdapter(ServerPlayer.class).getUser(player);
                currentUser.data().add(InheritanceNode.builder().group(crownGroup).build());
                lp.getUserManager().saveUser(currentUser);
            }
        }

    }

    private static BlockPos spawnCrownAtAltar(MinecraftServer server, String message) {
        try {
            Level level = server.getLevel(ServerLevel.OVERWORLD);
            if (level != null) {
                SavedCrownPedestalData pedestalData = SavedCrownPedestalData.getSavedCrownPedestalData(server);
                BlockPos pedestalPos = pedestalData.getCrownPos();
                Vector3f crownScale = pedestalData.getScale().toVector3f();
                Vec3 entityPos = pedestalPos.getCenter();
                Display.ItemDisplay itemDisplay = (Display.ItemDisplay)EntityType.ITEM_DISPLAY.create(level, EntitySpawnReason.COMMAND);

                assert itemDisplay != null;

                itemDisplay.setItemStack(ModItems.CROWN.getDefaultInstance());
                itemDisplay.setInvulnerable(true);
                itemDisplay.setDeltaMovement((double)0.0F, (double)0.0F, (double)0.0F);
                itemDisplay.setPos(entityPos);
                itemDisplay.setTransformation(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new Quaternionf(), crownScale, new Quaternionf()));
                level.addFreshEntity(itemDisplay);
                Interaction interaction = (Interaction)EntityType.INTERACTION.create(level, EntitySpawnReason.COMMAND);

                assert interaction != null;

                interaction.setPos(itemDisplay.getX(), itemDisplay.getY() - 0.485 * (double)crownScale.y, itemDisplay.getZ());
                interaction.setHeight(0.18F * crownScale.y * 1.05F);
                interaction.setWidth(0.5F * crownScale.x);
                level.addFreshEntity(interaction);
                broadcastGlobalMessage(server, message);
                return pedestalPos;
            } else {
                return BlockPos.ZERO;
            }
        } catch (Exception e) {
            TheCrown.LOGGER.error("An error in spawnCrownAtAltar occurred: {}", String.valueOf(e));
            return BlockPos.ZERO;
        }
    }

    private static BlockPos spawnCrownAtAltar(MinecraftServer server) {
        return spawnCrownAtAltar(server, "The Crown returns to its altar!");
    }

    private static int spawnCrownCommand(CommandContext<CommandSourceStack> context) {
        BlockPos pos = spawnCrownAtAltar(((CommandSourceStack)context.getSource()).getServer());
        ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Spawned crown at altar. " + String.valueOf(pos)).withStyle(ChatFormatting.GOLD), false);
        return 1;
    }

    /// Removes a player's crown & related permissions (assuming they have one to remove).
    /// Note: this does not check if they have the crown.
    private static void removePlayerCrown(ServerPlayer plr) {
        if (plr != null) {
//            plr.getInventory().removeItem(plr.getItemBySlot(EquipmentSlot.HEAD));
            plr.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);

            plr.inventoryMenu.broadcastChanges();


            onLoseCrown(plr);
        }
    }

    private static int resignCrownCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer plr = ((CommandSourceStack)context.getSource()).getPlayer();
        if (plr != null) {
            removePlayerCrown(plr);
            spawnCrownAtAltar(((CommandSourceStack)context.getSource()).getServer(), "%s has resigned The Crown. The Crown returns to its altar!".formatted(plr.getPlainTextName()));
            return 1;
        } else {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Cannot resign crown, source running command is not a player.").withStyle(ChatFormatting.RED));
            return 0;
        }
    }

    /// Removes player permissions and waypoint style for the crown.
    /// This does NOT remove the item.
    private static void onLoseCrown(ServerPlayer player) {
        resetWaypointIcon(player.level(), player);
        if (TheCrown.hasLuckPerms) {
            LuckPerms lp = LuckPermsProvider.get();
            User user = lp.getPlayerAdapter(ServerPlayer.class).getUser(player);
            user.data().clear((node) -> node.getKey().equals("group.crown"));
            lp.getUserManager().saveUser(user);
        }

    }

    private static void giveCrown(ServerPlayer player) {
        ItemStack currentHelmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!currentHelmet.isEmpty()) {
            TheCrown.LOGGER.info("Moving {}'s helmet to their inventory", player.getPlainTextName());
            if (!player.getInventory().add(currentHelmet)) {
                TheCrown.LOGGER.info("Dropping {}'s helmet", player.getPlainTextName());
                player.drop(currentHelmet, false);
            }
        }

        player.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 20, 2));
        ServerLevel level = player.level();
        giveCrownIcon(level, player);
        SavedRecentKingData savedRecentKingData = SavedRecentKingData.getSavedRecentKingData(level.getServer());
        savedRecentKingData.setRecentKing(player.nameAndId());
        TheCrown.LOGGER.info("Gave the crown to {}", player.getPlainTextName());
        player.setItemSlot(EquipmentSlot.HEAD, ModItems.CROWN.getDefaultInstance());
        afterPlayerChangeLevel(player, (ServerLevel)null, level);
    }

    private static int giveCrownCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> nameAndIds = GameProfileArgument.getGameProfiles(context, "player");
        if (nameAndIds.size() > 1) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Cannot give crown, the crown can only be given to one person.").withStyle(ChatFormatting.RED));
            return 0;
        } else {
            NameAndId nameIdPair = (NameAndId)nameAndIds.iterator().next();
            ServerPlayer player = ((CommandSourceStack)context.getSource()).getServer().getPlayerList().getPlayer(nameIdPair.name());
            if (player != null && !ModItems.isWearingCrown(player)) {
                giveCrown(player);
                broadcastGlobalMessage(((CommandSourceStack)context.getSource()).getServer(), "%s has been force given The Crown!".formatted(player.getPlainTextName()));
                return 1;
            } else if (player != null && ModItems.isWearingCrown(player)) {
                ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Cannot give crown, target player already has the crown.").withStyle(ChatFormatting.RED));
                return 0;
            } else {
                ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Cannot give crown, target player is null.").withStyle(ChatFormatting.RED));
                return 0;
            }
        }
    }

    private static int forceRemoveCrownCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> nameAndIds = GameProfileArgument.getGameProfiles(context, "player");
        if (nameAndIds.size() > 1) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Cannot remove crown, the crown can only be removed to one person at a time.").withStyle(ChatFormatting.RED));
            return 0;
        } else {
            UUID playerUUID = nameAndIds.iterator().next().id();
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);


            if (player != null) {
                //Player is online
                if (ModItems.isWearingCrown(player)) {
                    //Player has crown

                    removePlayerCrown(player);

                    broadcastGlobalMessage(((CommandSourceStack)context.getSource()).getServer(), "%s's Crown has been force removed!".formatted(player.getPlainTextName()));
                    return 1;
                } else {
                    //Player isn't wearing the crown

                    //just remove perms, NOT THE ITEM
                    onLoseCrown(player);

                    context.getSource().sendSystemMessage(Component.literal("Cannot remove crown, target player does not have the crown. \nHowever, any residual permissions that this person may have had \nwhile owning The Crown has been removed.").withStyle(ChatFormatting.GOLD));
                    return 1;
                }
            } else {
                //Player is offline
                loseCrownLaterUUIDs.add(playerUUID);
                context.getSource().sendSystemMessage(Component.literal("Cannot remove crown, target player isn't online. \nThe Crown will be removed once they join back.").withStyle(ChatFormatting.GOLD));
                return 1;
            }


        }
    }

    /// helper method that removes the speed glitch from a player, and sends them a message for fail/succession
    private static int removeSpeedGlitch(ServerPlayer player, CommandContext<CommandSourceStack> context) {
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            context.getSource().sendFailure(Component.literal("Could not find %s's movement speed attribute.".formatted(player.getPlainTextName())).withStyle(ChatFormatting.RED));
            return 0;
        }

        // Repair the persisted vanilla base value without removing legitimate
        // sprinting, effect, or equipment modifiers.
        player.getAttributes().resetBaseValue(Attributes.MOVEMENT_SPEED);
        player.connection.send(new ClientboundUpdateAttributesPacket(player.getId(), List.of(speed)));
        context.getSource().sendSuccess(() -> Component.literal("%s's speed glitch has been removed.".formatted(player.getPlainTextName())).withStyle(ChatFormatting.BLUE), false);
        return 1;
    }

    /// command that removes the speed glitch from the player who runs it
    private static int removeSpeedGlitchCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        return removeSpeedGlitch(player, context);
    }

    /// command that removes the speed glitch from any player(s)
    private static int adminRemoveSpeedGlitchCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> nameAndIds = GameProfileArgument.getGameProfiles(context, "player");
        int result = 1;
        for (NameAndId nameAndId : nameAndIds) {
            ServerPlayer plr = context.getSource().getServer().getPlayerList().getPlayer(nameAndId.id());
            //if this has a fail result, the command will fail
            //the command will fail as long as at least ONE player has failed
            result = removeSpeedGlitch(plr, context);
        }
        return result;
    }

    private static int toggleBedBombCommand(CommandContext<CommandSourceStack> context) {
        boolean f = BoolArgumentType.getBool(context, "value");
        SavedBedBombData savedBedBombData = SavedBedBombData.getSavedBedBombData(((CommandSourceStack)context.getSource()).getServer());
        savedBedBombData.setBedBombsFlag(f);
        ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(f ? "Enabled bed bombs" : "Disabled bed bombs"), false);
        return 1;
    }

    private static void broadcastGlobalMessage(MinecraftServer server, String message) {
        for(ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvents.BEACON_ACTIVATE), SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F, player.getRandom().nextLong()));
        }

        Component compMessage = Component.literal(message).withStyle(ChatFormatting.GOLD);
        server.getPlayerList().broadcastSystemMessage(compMessage, false);
        server.sendSystemMessage(compMessage);
        ActionbarManager.queue(compMessage, 200);
    }

    public void onInitializeServer() {
        TheCrown.LOGGER.info("Crown Events INIT!!");
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, commandSelection) -> {
            dispatcher.register(Commands.literal("movecrownpedestal").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)Commands.argument("scale", Vec3Argument.vec3()).requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))).executes(ServerModEvents::moveCrownPosCommand))));
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawncrown").requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))).executes(ServerModEvents::spawnCrownCommand));
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clearcrownsfromaltar").requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))).executes((context) -> {
                ServerLevel level = ((CommandSourceStack)context.getSource()).getLevel();
                level.getEntities(EntityType.ITEM_DISPLAY, (e) -> true).forEach((e) -> e.kill(level));
                level.getEntities(EntityType.INTERACTION, (e) -> true).forEach((e) -> e.kill(level));
                ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Cleared crowns from altar.").withStyle(ChatFormatting.GOLD), false);
                return 1;
            }));
            dispatcher.register((LiteralArgumentBuilder)Commands.literal("givecrown")
                    .then(((RequiredArgumentBuilder)Commands
                            .argument("player", GameProfileArgument.gameProfile())
                            .requires(
                                    (source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
                    )
                    .executes(ServerModEvents::giveCrownCommand)));
            dispatcher.register(Commands.literal("togglebedbombs").then(((RequiredArgumentBuilder)Commands.argument("value", BoolArgumentType.bool()).requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))).executes(ServerModEvents::toggleBedBombCommand)));
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("resigncrown").requires((stack) -> {
                if (TheCrown.hasLuckPerms) {
                    LuckPerms api = LuckPermsProvider.get();
                    ServerPlayer player = stack.getPlayer();
                    if (player != null) {
                        User user = api.getPlayerAdapter(ServerPlayer.class).getUser(stack.getPlayer());
                        return user.getCachedData().getPermissionData().checkPermission("group.crown").asBoolean();
                    }
                }

                return false;
            })).executes(ServerModEvents::resignCrownCommand));
            dispatcher.register(Commands.literal("forceremovecrown")
                    .then(Commands
                            .argument("player", GameProfileArgument.gameProfile())
                            .requires(
                                    (source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
                            .executes(ServerModEvents::forceRemoveCrownCommand)));
            dispatcher.register(Commands.literal("removespeedglitch")
                    .executes(ServerModEvents::removeSpeedGlitchCommand));
            dispatcher.register(Commands.literal("fremovespeedglitch")
                    .then(Commands
                            .argument("player", GameProfileArgument.gameProfile())
                            .requires(
                                    (source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
                            .executes(ServerModEvents::adminRemoveSpeedGlitchCommand)));
        });
        UseEntityCallback.EVENT.register((UseEntityCallback)(player, level, hand, entity, hitResult) -> {
            ServerLevel serverLevel = (ServerLevel)level;
            if (entity instanceof Interaction) {
                SavedRecentKingData savedRecentKingData = SavedRecentKingData.getSavedRecentKingData(serverLevel.getServer());
                if (savedRecentKingData.getRecentKing().equals(player.nameAndId())) {
                    player.sendSystemMessage(Component.literal("Nice try, but you cannot have The Crown consecutively!").withStyle(ChatFormatting.RED));
                    return InteractionResult.PASS;
                } else {
                    serverLevel.getEntities(EntityType.ITEM_DISPLAY, (e) -> true).forEach((e) -> e.kill(serverLevel));
                    serverLevel.getEntities(EntityType.INTERACTION, (e) -> true).forEach((e) -> e.kill(serverLevel));
                    giveCrown((ServerPlayer)player);
                    broadcastGlobalMessage(serverLevel.getServer(), "%s has picked up The Crown from the altar!".formatted(player.getPlainTextName()));
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.PASS;
            }
        });
        ServerLevelEvents.LOAD.register((ServerLevelEvents.Load)(server, level) -> {
            if (TheCrown.hasLuckPerms) {
                overworldSuffix = (SuffixNode)((SuffixNode.Builder)SuffixNode.builder(" [OVERWORLD]", 100).value(true)).build();
                netherSuffix = (SuffixNode)((SuffixNode.Builder)SuffixNode.builder(" [NETHER]", 100).value(true)).build();
                endSuffix = (SuffixNode)((SuffixNode.Builder)SuffixNode.builder(" [END]", 100).value(true)).build();
            }

        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer victim && source.getEntity() instanceof ServerPlayer attacker && attacker != victim && ModItems.isWearingCrown(victim)) {
                // set the combat log time
                victim.setAttached(ModAttachments.COMBAT_LOG_ATTACHMENT, Instant.now().getEpochSecond());


                if (TheCrown.hasLuckPerms) {
                    //remove the ability to warp, since this person is now in combat
                    LuckPerms lp = LuckPermsProvider.get();
                    User currentUser = lp.getPlayerAdapter(ServerPlayer.class).getUser(victim);

                    //set warp perms to false
                    currentUser.data().add(PermissionNode.builder().permission("blossom").value(false).build());
                    currentUser.data().add(PermissionNode.builder().permission("blossom.warps.warp").value(false).build());
                    currentUser.data().add(PermissionNode.builder().permission("blossom.tpa").value(false).build());

                    lp.getUserManager().saveUser(currentUser);
                }

            }

            return true;
        });

        ServerPlayerEvents.LEAVE.register(player -> {
            long lastCombatLogTime = player.getAttachedOrSet(ModAttachments.COMBAT_LOG_ATTACHMENT, 0L).longValue();

            //if the current epoch - the last recorded damage epoch is less than or equal to 30 seconds, then they have combat logged!!!
            if (Instant.now().getEpochSecond() - lastCombatLogTime < 30) {
                MinecraftServer server = player.level().getServer();
                //remove crown from person (this will happen upon rejoin)
                loseCrownLaterUUIDs.add(player.getUUID());
                //remove the player's combat log status
                player.setAttached(ModAttachments.COMBAT_LOG_ATTACHMENT, 0L);

                //give crown to whoever got kill credit
                LivingEntity attacker = player.getKillCredit();
                if (attacker instanceof ServerPlayer attackerPlayer) {
                    giveCrown(attackerPlayer);
                    broadcastGlobalMessage(server, "%s combat logged with the crown. %s now has The Crown!".formatted(player.getPlainTextName(), attackerPlayer.getPlainTextName()));
                } else {
                    // there is no kill credit attached to the combat log
                    // sometimes, this could happen if someone was hit into combat, but escaped & logged after the kill credit went away

                    spawnCrownAtAltar(server, "%s combat logged with the crown. The Crown returns to its altar!".formatted(player.getPlainTextName()));
                }

                TheCrown.LOGGER.info("{} combat logged with the crown!", player.getPlainTextName());
            }
        });

        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (UUID uuid : new ArrayList<>(removeCrownNextTickUUIDs)) {
                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                if (player != null) {
                    if (ModItems.isWearingCrown(player)) {
                        removePlayerCrown(player);
                    } else {
                        onLoseCrown(player);
                    }
                    removeCrownNextTickUUIDs.remove(uuid);
                }
            }

            ActionbarManager.tick(server);
        });
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register(ServerModEvents::afterPlayerChangeLevel);
        ServerPlayerEvents.JOIN.register((player) -> {
            for (UUID uuid : new ArrayList<>(loseCrownLaterUUIDs)){
                //if that player is on the lose crown list
                if (uuid.equals(player.getUUID())) {
                    //Wait until the next server tick so the client receives the equipment update.
                    if (!removeCrownNextTickUUIDs.contains(uuid)) {
                        removeCrownNextTickUUIDs.add(uuid);
                    }
                    loseCrownLaterUUIDs.remove(uuid);
                }
            }

            // AFTER we have (possibly) removed the crown, proccess their grown perms
            // it is IMPERITIVE that this happens AFTERWARDS
            afterPlayerChangeLevel(player, null, player.level());
        });
        ServerLivingEntityEvents.ALLOW_DEATH.register((ServerLivingEntityEvents.AllowDeath)(entity, damageSource, damageAmount) -> {
            LivingEntity cause = entity.getKillCredit();
            if (cause != null && cause.is(entity)) {
                cause = null;
            }

            if (cause != null && ModItems.isWearingCrown(cause) && entity instanceof ServerPlayer && !entity.getItemInHand(InteractionHand.MAIN_HAND).is(Items.TOTEM_OF_UNDYING) && !entity.getItemInHand(InteractionHand.OFF_HAND).is(Items.TOTEM_OF_UNDYING)) {
                TheCrown.LOGGER.info("{} got a kill with the crown", cause.getPlainTextName());
                CrownMobEffectUtil effect = this.possibleCrownEffects[cause.getRandom().nextIntBetweenInclusive(0, this.possibleCrownEffects.length - 1)];
                effect.apply(cause);
                return true;
            } else {
                if (entity instanceof ServerPlayer) {
                    TheCrown.LOGGER.info("player death detected");

                    ServerPlayer victim = (ServerPlayer)entity;
                    if (ModItems.isWearingCrown(entity)) {
                        //if the player has not cracked their crown AND the damage source wasnt one that bypasses totems..
                        if (entity.getItemBySlot(EquipmentSlot.HEAD).get(DataComponents.DEATH_PROTECTION) != null && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                            return true;
                        }

                        cause = cause instanceof ServerPlayer ? cause : null;
                        ServerLevel level = victim.level();
                        MinecraftServer server = level.getServer();
                        TheCrown.LOGGER.info("{} just died with the crown! (from equipment change detection). Cause: {}", victim.getPlainTextName(), cause == null ? "NATURAL" : cause.getPlainTextName());
                        onLoseCrown(victim);
                        if (cause == null) {
                            spawnCrownAtAltar(server);
                        } else {
                            String message = String.format("%s has killed %s and now has The Crown!", cause.getPlainTextName(), victim.getPlainTextName());
                            broadcastGlobalMessage(server, message);
                            giveCrown((ServerPlayer)cause);
                            cause.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 2));
                        }
                    }
                }

                return true;
            }
        });
        DefaultItemComponentEvents.MODIFY.register((DefaultItemComponentEvents.ModifyCallback)(modifyContext) -> modifyContext.modify(ModItems.CROWN, ModItems::crownDefaultItemComponents));
        DefaultItemComponentEvents.MODIFY.register((DefaultItemComponentEvents.ModifyCallback)(modifyContext) -> modifyContext.modify(ModItems.CRACKED_CROWN, ModItems::crownDefaultItemComponents));
    }


}
