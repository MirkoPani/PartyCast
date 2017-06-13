//shakeMinigame
var shakeMinigame={
    preload:function(){

    },
    create:function () {
        game.gamePhaser.stage.backgroundColor = "#ce4e29";
        game.minigameManager.drawHud();
        game.minigameManager.showInstructions("Agita in velocità!");
    }
}