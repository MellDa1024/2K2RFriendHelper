package com.mellda

import com.lambda.client.plugin.api.Plugin
import com.mellda.commands.MemberCommand
import com.mellda.managers.MemberManager

internal object TwoKTwoRPlugin : Plugin() {
    override fun onLoad() {
        commands.add(MemberCommand)
        managers.add(MemberManager)
    }
}