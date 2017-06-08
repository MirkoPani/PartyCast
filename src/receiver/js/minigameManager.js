//minigameManager
function MinigameManager() {
    this.time = 60;
    this.minigameCount = 0;
    this.currentMinigame = "";
};

MinigameManager.minigames=["gyroMinigame","micMinigame","shakeMinigame","touchMinigame"];

//Carica un minigame specifico
MinigameManager.prototype.loadSpecificMinigame = function (minigameType) {
    game.gameManager.updateGameData({ minigame: minigameType }, true);
    this.minigameCount++;
    game.gamePhaser.state.start(minigameType);
    this.currentMinigame = minigameType;

}

//Carica un minigame random
MinigameManager.prototype.loadRandomMinigame = function () {
    this.loadSpecificMinigame(this.getRandomMinigame());
}

MinigameManager.prototype.drawHud = function () {
    console.log("drawhud");
    this.drawMinigameCountText();
    this.drawPlayersHud();
}

MinigameManager.prototype.hideHud = function () {
    this.minigameCountText.visible = false;
}


//Ritorna una stringa random tra i minigame presenti
MinigameManager.prototype.getRandomMinigame=function() {
    return MinigameManager.minigames[Math.floor(Math.random()*MinigameManager.minigames.length)];
}

MinigameManager.prototype.drawPlayersHud = function () {
    console.log("drawplayershud");
    this.restorePlayers();

    for (var i = 0; i < game.playerManager.players.length; i++) {
        game.playerManager.setPlayerPosition(game.playerManager.players[i].Id, (window.innerWidth / 5) * (i + 1), window.innerHeight-(window.innerHeight/25));
        game.playerManager.setNamePosition(game.playerManager.players[i].Id,game.playerManager.players[i].sprite.position.x+ game.playerManager.players[i].sprite.width,game.playerManager.players[i].sprite.position.y);
        game.playerManager.showPlayer(game.playerManager.players[i].Id, true);
      /*  console.log(game.playerManager.players[i]);
        console.log(game.playerManager.players[i].sprite.visible);
        console.log(game.playerManager.players[i].sprite.position);
 */   }
}

MinigameManager.prototype.restorePlayers = function () {
    for (var i = 0; i < game.playerManager.players.length; i++) {
        game.gamePhaser.add.existing(game.playerManager.players[i].sprite);
     //   game.gamePhaser.add.existing(game.playerManager.players[i].shadow);
        game.gamePhaser.add.existing(game.playerManager.players[i].nameTitle);

        game.playerManager.players[i].sprite.scale.setTo(0.15, 0.15);
        game.playerManager.players[i].sprite.anchor.setTo(0.5,1);
        game.playerManager.players[i].nameTitle.anchor.setTo(0,1);
    }
}

MinigameManager.prototype.drawMinigameCountText = function () {
    this.minigameCountText = game.gamePhaser.add.text(window.innerWidth / 40, window.innerHeight / 20, "#" + this.minigameCount, game.textStyles.titleStyle);
    this.minigameCountText.fontSize = 50;
}

MinigameManager.prototype.showInstructions = function () {

}

MinigameManager.prototype.startTime = function () {

}

MinigameManager.prototype.setTime = function (time) {
    this.time = time;
}


