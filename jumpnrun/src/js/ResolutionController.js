class ResolutionController {
    constructor() {
        this.targetWidth = 1920;
        this.targetHeight = 1080;
        this.container = document.getElementById('resolution-container');
        this.init();
    }

    init() {
        this.updateScale();
        window.addEventListener('resize', () => {
            this.updateScale();
            // Notificar os cenários sobre a mudança de escala
            if (window.jogo && window.jogo.cenario1) {
                window.jogo.cenario1.calcularFatorEscala();
                window.jogo.cenario2.calcularFatorEscala();
            }
        });
    }

    updateScale() {
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        
        const scaleX = windowWidth / this.targetWidth;
        const scaleY = windowHeight / this.targetHeight;
        const scale = Math.min(scaleX, scaleY);
        
        this.container.style.transform = `scale(${scale})`;
    }

    getScale() {
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        const scaleX = windowWidth / this.targetWidth;
        const scaleY = windowHeight / this.targetHeight;
        return Math.min(scaleX, scaleY);
    }

    getCurrentWidth() {
        return window.innerWidth;
    }
}