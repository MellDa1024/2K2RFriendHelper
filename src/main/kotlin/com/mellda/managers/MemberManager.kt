package com.mellda.managers

import com.lambda.client.manager.Manager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.lambda.client.LambdaMod
import com.lambda.client.capeapi.UUIDUtils
import com.lambda.client.commons.extension.synchronized
import com.lambda.client.manager.managers.FriendManager
import com.lambda.client.manager.managers.UUIDManager
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.client.util.threads.defaultScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader

object MemberManager : Manager {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private var memberFile = MemberFile()
    private const val prefix = "[2K2RFriendHelper]"

    fun addMember(file : File): Boolean {
        return try {
            memberFile = gson.fromJson(FileReader(file), object : TypeToken<MemberFile>() {}.type)
            defaultScope.launch {
                MessageSendHelper.sendChatMessage("$prefix Found ${memberFile.members.size} Members, appending to FriendList...")
                for (uuid in memberFile.members) {
                    UUIDUtils.fixUUID(uuid)?.let{ fixedUUID ->
                        UUIDManager.getByUUID(fixedUUID)?.let {
                            if (FriendManager.isFriend(it.name)) MessageSendHelper.sendChatMessage("$prefix &7${it.name}&r is already your friend.")
                            else if (FriendManager.addFriend(it.name)) MessageSendHelper.sendChatMessage("$prefix &7${it.name}&r has been friended.")
                        } ?: MessageSendHelper.sendChatMessage("$prefix There aren't any player who has UUID \"${fixedUUID}\".")
                    } ?: MessageSendHelper.sendChatMessage("$prefix $uuid is not a valid UUID.")
                    delay(500L)
                }
                for (uuid in memberFile.blacklist) {
                    UUIDUtils.fixUUID(uuid)?.let{ fixedUUID ->
                        UUIDManager.getByUUID(fixedUUID)?.let {
                            if (FriendManager.isFriend(it.name)){
                                if(FriendManager.removeFriend(it.name)){
                                    MessageSendHelper.sendChatMessage("$prefix blacklisted &4${it.name}&r has been removed.")
                                }
                            }
                        } ?: MessageSendHelper.sendChatMessage("$prefix There aren't any player who has UUID \"${fixedUUID}\".")
                    } ?: MessageSendHelper.sendChatMessage("$prefix $uuid is not a valid UUID.")
                    delay(500L)
                }
                MessageSendHelper.sendChatMessage("$prefix Done!")
            }
            true
        } catch (e: Exception) {
            LambdaMod.LOG.warn("Failed loading data.json", e)
            false
        }
    }

    data class MemberFile(
        val members: MutableSet<String> = LinkedHashSet<String>().synchronized(),
        val blacklist: MutableSet<String> = LinkedHashSet<String>().synchronized()
    )
}