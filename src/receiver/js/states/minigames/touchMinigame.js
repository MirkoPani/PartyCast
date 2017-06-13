//touchMinigame
var touchMinigame={
    preload:function(){

    },
    create:function () {
        game.gamePhaser.stage.backgroundColor = "#3db3ce";
        game.minigameManager.drawHud();
        game.minigameManager.showInstructions("Tocca in velocità!");
    }
}