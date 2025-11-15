class ResolutionController {
    constructor() {
        this.targetWidth = 1920;
        this.targetHeight = 1080;
        this.container = document.getElementById('game-container');
        this.currentScale = 1;
        this.init();
    }

    init() {
        this.updateScale();
        window.addEventListener('resize', () => {
            this.updateScale();
            this.notificarMudancaEscala();
        });
    }

    updateScale() {
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        
        const scaleX = windowWidth / this.targetWidth;
        const scaleY = windowHeight / this.targetHeight;
        const scale = Math.min(scaleX, scaleY);
        
        const minScale = 0.4;
        const maxScale = 1.5;
        this.currentScale = Math.max(minScale, Math.min(scale, maxScale));
        
        this.container.style.transform = `translate(-50%, -50%) scale(${this.currentScale})`;
    }

    notificarMudancaEscala() {
        const evento = new CustomEvent('escalaAtualizada', {
            detail: { 
                scale: this.currentScale,
                width: window.innerWidth,
                height: window.innerHeight
            }
        });
        window.dispatchEvent(evento);
    }

    getFatorVelocidade() {
        return 1 / this.currentScale;
    }

    getScale() {
        return this.currentScale;
    }

    getCurrentWidth() {
        return window.innerWidth;
    }

    getCurrentHeight() {
        return window.innerHeight;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    if (!window.resolutionController) {
        window.resolutionController = new ResolutionController();
    }
});