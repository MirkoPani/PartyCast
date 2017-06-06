//lobby
var lobbyState = {
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#96ceb4";

        //Settiamo la lobby open
        game.gameManager.updateLobbyState(
            cast.receiver.games.LobbyState.OPEN, null);

        //Scritta Lobby
        var lobbyTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 80, "Lobby", game.textStyles.titleStyle);
        lobbyTitle.anchor.setTo(0.5, 0.5);
        lobbyTitle.fontSize = 100;

        //Scritta: in attesa di altri giocatori
        var waitingTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 180, "In attesa di altri giocatori...", game.textStyles.normalStyle);
        waitingTitle.anchor.setTo(0.5, 0.5);
        waitingTitle.fontSize = 50;

        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_READY,
            function (event) {
                console.log("Ricevuto playerReady. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerReady(event.playerInfo.playerId, event.requestExtraMessageData);
            });

        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_DROPPED,
            function (event) {
                console.log("Ricevuto playerDropped. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerDropped(event.playerInfo.playerId, event.requestExtraMessageData);
            });

        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_QUIT,
            function (event) {
                console.log("Ricevuto playerQuit. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerQuit(event.playerInfo.playerId, event.requestExtraMessageData);
            });

    },
    handlePlayerReady: function (playerId, playerData) {
        game.gameManager.updatePlayerData(playerId, playerData);
        game.playerManager.addPlayerToList(playerId, playerData.Name, playerData.Avatar);
        game.playerManager.setPlayerPosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 400);
        game.playerManager.setNamePosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 550);
        game.playerManager.showPlayer(playerId, true);
        game.playerManager.showShadowUnderPlayer(playerId, true);
    },
    handlePlayerDropped: function (playerId, playerData) {

        game.playerManager.removePlayerFromList(playerId);
        //Riposizioniamo tutti gli altri giocatori
        for (var i = 0; i < game.playerManager.players.length; i++) {
            var id = game.playerManager.players[i].Id;
            game.playerManager.setPlayerPosition(id, (window.innerWidth / 5) * (i + 1), 400);
            game.playerManager.setNamePosition(id, (window.innerWidth / 5) * (i + 1), 550);
        }

    },
    handlePlayerQuit: function (playerId, playerData) {

        game.playerManager.removePlayerFromList(playerId);
        //Riposizioniamo tutti gli altri giocatori
        for (var i = 0; i < game.playerManager.players.length; i++) {
            var id = game.playerManager.players[i].Id;
            game.playerManager.setPlayerPosition(id, (window.innerWidth / 5) * (i + 1), 400);
            game.playerManager.setNamePosition(id, (window.innerWidth / 5) * (i + 1), 550);
        }

    }


}