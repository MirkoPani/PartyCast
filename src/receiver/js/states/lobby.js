//lobby
var lobbyState= {
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#96ceb4";

        //Scritta Lobby
        var lobbyTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX,80,"Lobby",game.textStyles.titleStyle);
        lobbyTitle.anchor.setTo(0.5,0.5);
        lobbyTitle.fontSize=100;

        //Scritta: in attesa di altri giocatori
        var waitingTitle=game.gamePhaser.add.text(game.gamePhaser.world.centerX,180,"In attesa di altri giocatori...",game.textStyles.normalStyle);
        waitingTitle.anchor.setTo(0.5,0.5);
        waitingTitle.fontSize=50;

        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_READY,
            function(event) {
                console.log("Ricevuto playerReady");
                lobbyState.addPlayer(event.playerInfo.playerId,event.requestExtraMessageData);
            });

    },
    addPlayer:function(playerId,playerData) {
        game.gameManager.updatePlayerData(playerId, playerData);
        game.playerManager.addPlayer(playerId,playerData.Name,playerData.Avatar);
        game.playerManager.setPlayerPosition(1,400,400);
        game.playerManager.showPlayer(1,true);

        game.playerManager.showShadowUnderPlayer(1,true);
    }

}