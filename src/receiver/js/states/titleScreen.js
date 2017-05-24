//titleScreen
var titleScreenState= {

    preload: function () {
        game.load.image("background", "img/background.png");
    },

    create: function () {
        this.background= game.add.tileSprite(0, 0, window.innerWidth, window.innerHeight, 'background');
        var bar = game.add.graphics();

        bar.beginFill(0x000000, 0.2);
        bar.drawRect(0,  window.innerHeight/2-50, window.innerWidth, 100);

        var style = { font: "bold 32px myFirstFont", fill: "#fff", boundsAlignH: "center", boundsAlignV: "middle" };

        //  The Text is positioned at 0, 100
        text = game.add.text(0, 0, "PartyCast", style);
        text.setShadow(3, 3, 'rgba(0,0,0,0.5)', 2);

        //  We'll set the bounds to be from x0, y100 and be 800px wide by 100px high
        text.setTextBounds(0,  window.innerHeight/2-50, window.innerWidth, 100);

        //Solo per debug
        var wkey= game.input.keyboard.addKey(Phaser.Keyboard.W);
        wkey.onDown.addOnce(this.start,this);
    },

    start: function () {
        game.state.start("lobby");
    },

    update:function() {
        this.background.tilePosition.y += 0.5;
        this.background.tilePosition.x += 0.5;
    }
}