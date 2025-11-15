class ResolutionController {
    constructor() {
        this.targetWidth = 1920;
        this.targetHeight = 1080;
        this.container = document.getElementById('game-container');
        this.resolutionContainer = document.getElementById('resolution-container');
        this.currentScale = 1;
        this.init();
    }

    init() {
        this.updateScale();
        window.addEventListener('resize', () => {
            this.updateScale();
            this.notificarMudancaEscala();
        });
        
        // Notificar inicialização
        setTimeout(() => this.notificarMudancaEscala(), 100);
    }

    updateScale() {
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        
        const scaleX = windowWidth / this.targetWidth;
        const scaleY = windowHeight / this.targetHeight;
        const scale = Math.min(scaleX, scaleY);
        
        // Limitar escala mínima e máxima para evitar problemas
        const minScale = 0.3;
        const maxScale = 1.5;
        const finalScale = Math.max(minScale, Math.min(scale, maxScale));
        
        this.currentScale = finalScale;
        
        // Aplicar transformação diretamente no container do jogo
        if (this.container) {
            this.container.style.transform = `translate(-50%, -50%) scale(${finalScale})`;
        }
    }

    getScale() {
        return this.currentScale;
    }

    getCurrentWidth() {
        return window.innerWidth;
    }

    // Método para converter coordenadas baseadas na escala
    scaleValue(value) {
        return value * this.currentScale;
    }

    // Método para reverter coordenadas baseadas na escala
    unscaleValue(value) {
        return value / this.currentScale;
    }

    // Notificar todos os sistemas sobre mudança de escala
    notificarMudancaEscala() {
        const evento = new CustomEvent('resolutionChanged', {
            detail: { 
                scale: this.currentScale,
                width: window.innerWidth,
                height: window.innerHeight
            }
        });
        window.dispatchEvent(evento);
    }

    // Método para obter a posição real de elementos considerando a escala
    getScaledPosition(element) {
        if (!element) return { x: 0, y: 0 };
        
        const rect = element.getBoundingClientRect();
        const containerRect = this.container.getBoundingClientRect();
        
        return {
            x: (rect.left - containerRect.left) / this.currentScale,
            y: (containerRect.bottom - rect.bottom) / this.currentScale
        };
    }
}