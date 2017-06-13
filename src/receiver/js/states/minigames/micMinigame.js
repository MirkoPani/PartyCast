//micMinigame
var micMinigame={
    preload:function(){

    },
    create:function () {
        game.gamePhaser.stage.backgroundColor = "#69ce30";
        game.minigameManager.drawHud();
        game.minigameManager.showInstructions("Soffia in velocità!");
    }
}