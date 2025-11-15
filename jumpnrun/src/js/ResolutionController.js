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
        
        // Forçar atualização inicial
        setTimeout(() => {
            this.forceUpdate();
        }, 500);
    }

    updateScale() {
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        
        const scaleX = windowWidth / this.targetWidth;
        const scaleY = windowHeight / this.targetHeight;
        const scale = Math.min(scaleX, scaleY);
        
        // Limites mais amplos para melhor responsividade
        const minScale = 0.3;   // 30% do tamanho original
        const maxScale = 2.0;   // 200% do tamanho original
        const finalScale = Math.max(minScale, Math.min(scale, maxScale));
        
        this.currentScale = finalScale;
        
        // Aplicar transformação diretamente no container do jogo
        if (this.container) {
            this.container.style.transform = `translate(-50%, -50%) scale(${finalScale})`;
        }
        
        console.log(`🔄 Escala atualizada: ${finalScale.toFixed(2)} (Tela: ${windowWidth}x${windowHeight})`);
    }

    getScale() {
        return this.currentScale;
    }

    // NOVO: Método para obter fator de velocidade (INVERSO da escala)
    getFatorVelocidade() {
        return 1 / this.currentScale;
    }

    getCurrentWidth() {
        return window.innerWidth;
    }

    // Notificar todos os sistemas sobre mudança de escala
    notificarMudancaEscala() {
        const evento = new CustomEvent('resolutionChanged', {
            detail: { 
                scale: this.currentScale,
                fatorVelocidade: this.getFatorVelocidade(),
                width: window.innerWidth,
                height: window.innerHeight
            }
        });
        window.dispatchEvent(evento);
        
        console.log(`📢 Notificação de escala: ${this.currentScale.toFixed(2)} | Fator Velocidade: ${this.getFatorVelocidade().toFixed(2)}`);
    }

    // Método para forçar atualização imediata
    forceUpdate() {
        this.updateScale();
        this.notificarMudancaEscala();
    }

    // DEBUG: Método para testar velocidades
    debugVelocidades() {
        console.log('=== DEBUG VELOCIDADES ===');
        console.log(`Escala: ${this.currentScale.toFixed(2)}`);
        console.log(`Fator Velocidade: ${this.getFatorVelocidade().toFixed(2)}`);
        console.log(`Velocidade Base (0.18): ${(0.18 * this.getFatorVelocidade()).toFixed(3)}`);
        console.log(`Velocidade Máxima (0.3): ${(0.3 * this.getFatorVelocidade()).toFixed(3)}`);
    }
}