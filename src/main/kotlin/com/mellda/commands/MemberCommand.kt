package com.mellda.commands

import com.lambda.client.LambdaMod
import com.lambda.client.command.ClientCommand
import com.lambda.client.util.FolderUtils
import com.lambda.client.util.text.MessageSendHelper
import com.mellda.managers.MemberManager
import java.io.File

object MemberCommand : ClientCommand(
    name = "2k2rMember",
    description = "Adds 2K2R Member to Friend List."
) {
    private const val prefix = "[2K2RFriendHelper]"
    init {
        literal("update") {
            execute("Adds 2K2R Member from data.json") {
                try {
                    val file = File(FolderUtils.lambdaFolder + "data.json")
                    if (file.isFile) {
                        MessageSendHelper.sendChatMessage("$prefix Trying to add 2K2R Members to FriendList, It may take some time...")
                        MemberManager.addMember(file)
                    }
                    else MessageSendHelper.sendChatMessage("$prefix Can't find data.json. Please put your data.json file in Lambda folder.")
                } catch (e : Exception) {
                    MessageSendHelper.sendErrorMessage("An unexpected error occurred. see log for more info.")
                    LambdaMod.LOG.warn(e)
                }
            }
        }
    }
}