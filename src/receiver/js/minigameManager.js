//minigameManager
function MinigameManager() {
    this.time = 60;
    this.minigameCount = 0;
    this.currentMinigame = "";


    this.pointsTable={};
    this.minigamePointsTableText="";
        this.pointsTableText="";
       this.totalPointsTableText="";
};
//"gyroMinigame", "micMinigame",
MinigameManager.minigames = ["shakeMinigame", "touchMinigame"];

//Carica un minigame specifico
MinigameManager.prototype.loadSpecificMinigame = function (minigameType) {
    game.gameManager.updateGameData({minigame: minigameType});
    this.minigameCount++;
    game.gamePhaser.state.start(minigameType);
    this.currentMinigame = minigameType;

};

//Carica un minigame random
MinigameManager.prototype.loadRandomMinigame = function () {
   // this.loadSpecificMinigame(this.getRandomMinigame());
    this.loadSpecificMinigame(MinigameManager.minigames[game.minigameManager.minigameCount % MinigameManager.minigames.length]);
};

MinigameManager.prototype.drawHud = function () {
    console.log("drawhud");
    this.drawMinigameCountText();
    this.drawPlayersHud();
};

MinigameManager.prototype.hideHud = function () {
    this.minigameCountText.visible = false;
};


//Ritorna una stringa random tra i minigame presenti
MinigameManager.prototype.getRandomMinigame = function () {
    return MinigameManager.minigames[Math.floor(Math.random() * MinigameManager.minigames.length)];
};

MinigameManager.prototype.drawPlayersHud = function () {
    console.log("drawplayershud");
    this.restorePlayers();

    for (var i = 0; i < game.playerManager.players.length; i++) {
        game.playerManager.setPlayerPosition(game.playerManager.players[i].Id, (window.innerWidth / 5) * (i + 1), window.innerHeight - (window.innerHeight / 25));
        game.playerManager.setNamePosition(game.playerManager.players[i].Id, game.playerManager.players[i].sprite.position.x + game.playerManager.players[i].sprite.width, game.playerManager.players[i].sprite.position.y);
        game.playerManager.showPlayer(game.playerManager.players[i].Id, true);
        /*  console.log(game.playerManager.players[i]);
         console.log(game.playerManager.players[i].sprite.visible);
         console.log(game.playerManager.players[i].sprite.position);
         */
    }
};

MinigameManager.prototype.restorePlayers = function () {
    for (var i = 0; i < game.playerManager.players.length; i++) {
        game.gamePhaser.add.existing(game.playerManager.players[i].sprite);
        //   game.gamePhaser.add.existing(game.playerManager.players[i].shadow);
        game.gamePhaser.add.existing(game.playerManager.players[i].nameTitle);

        game.playerManager.players[i].sprite.scale.setTo(0.15, 0.15);
        game.playerManager.players[i].sprite.anchor.setTo(0.5, 1);
        game.playerManager.players[i].nameTitle.anchor.setTo(0, 1);
    }
};

MinigameManager.prototype.drawMinigameCountText = function () {
    this.minigameCountText = game.gamePhaser.add.text(window.innerWidth / 40, window.innerHeight / 20, "#" + this.minigameCount, game.textStyles.titleStyle);
    this.minigameCountText.fontSize = 50;
};

MinigameManager.prototype.showInstructions = function (instructionString) {
    /*this.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, instructionString, game.textStyles.titleStyle);
    this.minigameInstructionText.fontSize = 80;
    this.minigameInstructionText.anchor.setTo(0.5, 0.5);

    //  Create our Timer
    timer = game.gamePhaser.time.create(false);

    //  Set a TimerEvent to occur after 3 seconds
    timer.add(3000, function () {
        this.minigameInstructionText.setText("3");
    }, this);
    timer.add(4000, function () {
        this.minigameInstructionText.setText("2");
    }, this);
    timer.add(5000, function () {
        this.minigameInstructionText.setText("1");
    }, this);
    timer.add(6000, function () {
        this.minigameInstructionText.setText("GO!");
        game.gamePhaser.add.tween(this.minigameInstructionText).to({alpha: 0}, 250, "Linear", true);

        if(this.currentMinigame=="touchMinigame"){
            var wrapper = document.getElementById("wrapper");
            wrapper.style.visibility = "visible" ;
        }
    }, this);
    //  Start the timer running - this is important!
    //  It won't start automatically, allowing you to hook it to button events and the like.
    timer.start();
*/
};

MinigameManager.prototype.startTime = function () {

};

MinigameManager.prototype.setTime = function (time) {
    this.time = time;
};


MinigameManager.prototype.handlePlayerDisconnected = function (event) {
    console.log("HandlePlayerDisconnected");
    game.playerManager.removePlayerFromList(event.playerInfo.playerId);
    game.minigameManager.drawPlayersHud();

    //Check numero di giocatori
    if (game.playerManager.players.length < 2) {
        this.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, window.innerHeight - (window.innerHeight / 25)-200, "Troppi pochi player! Ritorno alla lobby", game.textStyles.errorStyle);
        this.minigameInstructionText.fontSize = 30;
        this.minigameInstructionText.anchor.setTo(0.5, 0.5);

        //Resettiamo il gioco
        game.minigameManager.resetGame();
    }

}

//Riporta il gioco alla lobby e azzera i giocatori
MinigameManager.prototype.resetGame = function () {

    //Nascondiamo la griglia per il touchminigame
    var wrapper = document.getElementById("wrapper");
    wrapper.style.visibility = "hidden";

    //  Create our Timer
    timer = game.gamePhaser.time.create(false);
    timer.add(3000, function () {
        game.playerManager.resetPlayers();
        game.minigameManager.minigameCount = 0;
        game.playerManager.setHost("");
        game.gameManager.updateGameData({minigame: "none"}, true);


        game.gamePhaser.state.start("titleScreen");

    }, this);

    timer.start();


}
MinigameManager.prototype.showPointsTable=function(){

    var altezzaTab=window.innerHeight-(window.innerHeight*.4);
    var larghezzaTab= window.innerWidth-(window.innerWidth*.4);
    var posXTab=game.gamePhaser.world.centerX-(larghezzaTab/2);
    var posYTab=game.gamePhaser.world.centerY-(altezzaTab/2);

    var riservato=100;
    var fattore=(altezzaTab-riservato)/game.playerManager.players.length;
    var inizioLinea={X:posXTab+50,Y:posYTab+riservato};
    var fineLinea={X:posXTab+larghezzaTab-50,Y:posYTab+riservato};

    var lunghezzaLinea=fineLinea.Y-inizioLinea.Y;
    var centroLinea=posXTab+(larghezzaTab/2);
    var trequarti=centroLinea+(larghezzaTab/4);

    this.pointsTable = game.gamePhaser.add.graphics(0,0);
    this.pointsTable.lineStyle(1);

    //rettangolo
    this.pointsTable.beginFill(0x333333, 0.5);
    this.pointsTable.drawRect(posXTab, posYTab, larghezzaTab, altezzaTab);
    this.pointsTable.endFill();

    //Linea
    this.pointsTable.beginFill(0x666666, 1);
    this.pointsTable.moveTo(inizioLinea.X,inizioLinea.Y);
    this.pointsTable.lineTo(fineLinea.X,fineLinea.Y);
    this.pointsTable.endFill();

    //Minigame
    this.minigamePointsTableText=game.gamePhaser.add.text(centroLinea+20, posYTab+riservato/2, "Minigame", game.textStyles.titleStyle);
    this.minigamePointsTableText.fontSize = 25;
    this.minigamePointsTableText.anchor.setTo(0, 0.5);

    //Total
    this.totalPointsTableText=game.gamePhaser.add.text(trequarti+20, posYTab+riservato/2, "Total", game.textStyles.titleStyle);
    this.totalPointsTableText.fontSize = 25;
    this.totalPointsTableText.anchor.setTo(0, 0.5);

    //Points
    this.pointsTableText=game.gamePhaser.add.text(posXTab+20, posYTab+riservato/2, "Points", game.textStyles.titleStyle);
    this.pointsTableText.fontSize = 25;
    this.pointsTableText.anchor.setTo(0, 0.5);


    var miniPointsarrayText=[];
    var pointsArrayText=[]

    //Portiamo sopra
    game.gamePhaser.world.bringToTop(this.minigamePointsTableText);
    game.gamePhaser.world.bringToTop(this.totalPointsTableText);
    game.gamePhaser.world.bringToTop(this.pointsTableText);

    console.log("fattore: "+fattore);
    console.log("posY: "+posYTab);
    console.log("Altezza: "+altezzaTab);
    for (var i = 0; i < game.playerManager.players.length; i++) {
        var posy= posYTab+riservato+fattore* (i+1)-(game.playerManager.players[i].sprite.height/game.playerManager.players.length);
        game.playerManager.setPlayerPosition(game.playerManager.players[i].Id, posXTab+50,posy);
        game.playerManager.setNamePosition(game.playerManager.players[i].Id, posXTab+50 + game.playerManager.players[i].sprite.width, posYTab+riservato+fattore* (i+1)-(game.playerManager.players[i].sprite.height/game.playerManager.players.length));
        game.playerManager.showPlayer(game.playerManager.players[i].Id, true);

        //Mostriamo punti minigioco
        miniPointsarrayText.push(game.gamePhaser.add.text(centroLinea+50, posy,"+"+game.playerManager.players[i].minigamePoints, game.textStyles.titleStyle));
        miniPointsarrayText[i].fontSize = 25;
        miniPointsarrayText[i].anchor.setTo(0, 1);
        pointsArrayText.push(game.gamePhaser.add.text(trequarti+50, posy, game.playerManager.players[i].points, game.textStyles.titleStyle));
        pointsArrayText[i].fontSize = 25;
        pointsArrayText[i].anchor.setTo(0, 1);
    }

    //  Create our Timer
    var timer2 = game.gamePhaser.time.create(false);

    //  Set a TimerEvent to occur after 3 seconds
    timer2.add(4000, function () {

        game.playerManager.resetAndSumMinigamePoints();
        for (var i = 0; i < game.playerManager.players.length; i++) {
            //Mostriamo punti minigioco
            miniPointsarrayText[i].setText(game.playerManager.players[i].minigamePoints);

            pointsArrayText[i].setText(game.playerManager.players[i].points);
            pointsArrayText[i].setStyle(game.textStyles.nameStyle);
            pointsArrayText[i].fontSize = 25;

        }
    }, this);
    timer2.add(7000, function () {
        //CAMBIAGIOCO
        game.minigameManager.loadRandomMinigame();
    }, this);

    timer2.start();


}