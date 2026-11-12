package com.example.exampleplugin.util;

import org.bukkit.configuration.file.FileConfiguration;

public record Config(String playerInvalidMessage, String playerOnlyCommandMessage, String playerArgumentRequiredMessage,
                     String menuOpenFailureMessage, String itemAlreadyFullHealthMessage, String itemSuccessfullyRepairedMessage,
                     String itemNotDamageableMessage, String itemRepairFailedMessage, String tpaToSelfErrorMessage,
                     String tpaFailedRequestMessage, String tpaSentRequestMessage, String tpaReceivedRequestMessage,
                     String tpaCountdownMessage, String tpaTargetAcceptedMessage,String tpaTargetDeclinedMessage,
                     String tpaAcceptMessage, String tpaDeclineMessage, String godModeSetFailedMessage,
                     String godModeEnabledMessage, String godModeDisabledMessage, String targetEnterGodModeMessage,
                     String targetLeaveGodModeMessage, String setPersonalModeMessage, String gameModeUpdatedExternally,
                     String updatedTargetGameMode) {

    public static Config fromYaml(FileConfiguration yaml) {
        return new Config(
                // General
                yaml.getString("playerInvalidMessage", "Player is not valid or has disconnected."),
                yaml.getString("playerOnlyCommandMessage", "You must be a player to use this command!"),
                yaml.getString("playerArgumentRequiredMessage", "You must be a player to use this command without a player argument!"),
                yaml.getString("menuOpenFailureMessage", "An error occurred while trying to open the menu!"),


                // Fix Command
                yaml.getString("itemAlreadyFullHealthMessage", "Item is already at full health!"),
                yaml.getString("itemSuccessfullyRepairedMessage", "Successfully repaired item!"),
                yaml.getString("itemNotDamageableMessage", "Item in main hand is not damageable!"),
                yaml.getString("itemRepairFailedMessage", "An error occurred while trying to fix your item!"),

                // TPA
                yaml.getString("tpaToSelfErrorMessage", "You can't teleport to yourself!"),
                yaml.getString("tpaFailedRequestMessage", "An error occurred while making the teleport request!"),
                yaml.getString("tpaSentRequestMessage", "Successfully sent teleport request to {targetPlayer}"),
                yaml.getString("tpaReceivedRequestMessage", "{sourcePlayer} has requested to teleport to you, would you like to accept?"),
                yaml.getString("tpaCountdownMessage", "Teleporting you to {targetPlayer} in {timeRemaining}"),
                yaml.getString("tpaTargetAcceptedMessage", "{targetPlayer} accepted your teleport request"),
                yaml.getString("tpaTargetDeclinedMessage", "{targetPlayer} declined your teleport request"),
                yaml.getString("tpaAcceptMessage", "{targetPlayer} will be teleported to you shortly"),
                yaml.getString("tpaDeclineMessage", "{sourcePlayer}'s request has been declined"),

                // God Mode
                yaml.getString("godModeSetFailedMessage", "An error occurred while trying to set god mode!"),
                yaml.getString("godModeEnabledMessage", "You are now in god mode."),
                yaml.getString("godModeDisabledMessage", "You are no longer in god mode."),
                yaml.getString("targetEnterGodModeMessage", "{target} is now in god mode."),
                yaml.getString("targetLeaveGodModeMessage", "{target} is no longer in god mode."),

                // Game Mode
                yaml.getString("setPersonalModeMessage", "Successfully updated game mode to {gameMode}"),
                yaml.getString("gameModeUpdatedExternally", "{sourcePlayer} updated your game mode to {gameMode}."),
                yaml.getString("updatedTargetGameMode", "Successfully updated {targetPlayer}'s game mode to {gameMode}")
        );
    }
}