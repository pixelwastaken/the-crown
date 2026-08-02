package dirt.thecrown.dataattachment;

import com.mojang.serialization.Codec;
import dirt.thecrown.TheCrown;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public class ModAttachments {
    public static void initalize() {
        TheCrown.LOGGER.info("The Crown attachments loaded!");
    }

    public static final AttachmentType<Boolean> MUST_RESTORE_ITEMS_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(TheCrown.MOD_ID, "must_restore_items_attachment"), // The ID of your Attachment
            builder -> builder
                    .initializer(() -> false) // The default value of the Attachment, if one has not been set.
                    .persistent(Codec.BOOL) // Dictates how this Attachment's data should be saved and loaded.
    );
}
