//gyroMinigame
var gyroMinigame={
    preload:function(){

    },
    create:function () {
        game.gamePhaser.stage.backgroundColor = "#ce35be";
        game.minigameManager.drawHud();
        game.minigameManager.showInstructions("Ruota in velocità!");
    }
}