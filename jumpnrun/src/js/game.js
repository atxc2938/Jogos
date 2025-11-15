class Jogo {
    constructor() {
        this.container = document.getElementById('game-container');
        this.personagem = document.getElementById('personagem');
        
        // VELOCIDADES BASE EM 1920x1080
        this.velocidadeMenu1 = 0.035;
        this.velocidadeMenu2 = 0.09;
        this.velocidadeJogo1 = 0.07;
        this.velocidadeJogo2 = 0.18;
        
        this.cenario1 = new Cenario('segundo-plano', this.velocidadeMenu1, -477.3);
        this.cenario2 = new Cenario('segundo-plano-2', this.velocidadeMenu2, -144.2);
        this.personagemController = new PersonagemController(this.personagem);
        this.obstaculoController = new ObstaculoController();
        this.menuController = new MenuController();
        this.gameOverController = new GameOverController();
        this.configuracaoController = new ConfiguracaoController();
        this.timerController = new TimerController();
        this.resolutionController = new ResolutionController();
        this.jogoIniciado = false;
        this.dificuldadeAtual = null;
        this.tempoInicio = null;
        this.intervaloVelocidade = null;
        this.velocidadeMaxima = 0.3;
        this.incrementoVelocidade = 0.05;
        this.init();
    }

    init() {
        console.log('Jogo Jump N Run inicializado');
        this.cenario1.iniciarAnimacao();
        this.cenario2.iniciarAnimacao();
        
        // Escutar mudanças de resolução
        window.addEventListener('resolutionChanged', (event) => {
            this.atualizarTudoParaEscala(event.detail.scale);
        });
    }

    iniciarJogo(dificuldade) {
        if (this.jogoIniciado) return;
        
        this.jogoIniciado = true;
        this.dificuldadeAtual = dificuldade;
        this.tempoInicio = Date.now();
        
        this.personagem.style.opacity = '1';
        this.personagem.style.left = '250px';
        
        // CORREÇÃO: Ajustar TUDO para a escala atual
        const escalaAtual = this.resolutionController.getScale();
        this.atualizarTudoParaEscala(escalaAtual);
        
        this.obstaculoController.iniciar();
        this.iniciarAumentoVelocidade();
        this.personagemController.iniciarControles();
        this.timerController.iniciarTimer();
    }

    atualizarTudoParaEscala(escala) {
        if (!this.jogoIniciado) return;
        
        // CORREÇÃO DEFINITIVA: Fator quase igual em todas as resoluções
        const fatorVelocidade = 0.95 + (escala * 0.05); // Varia de 0.95 a 1.0
        
        console.log(`🎯 Atualizando tudo para escala ${escala.toFixed(2)} - Fator Velocidade: ${fatorVelocidade.toFixed(2)}`);
        
        // Atualizar cenários
        this.cenario1.setVelocidadeSuave(this.velocidadeJogo1 * fatorVelocidade, 200);
        this.cenario2.setVelocidadeSuave(this.velocidadeJogo2 * fatorVelocidade, 200);
        
        // Atualizar obstáculos
        this.obstaculoController.atualizarVelocidade(this.velocidadeJogo2 * fatorVelocidade);
        
        // Atualizar física do personagem
        this.personagemController.atualizarFisicaParaEscala(fatorVelocidade);
        
        // Atualizar validador de pulos
        this.obstaculoController.atualizarTudoParaEscala(fatorVelocidade);
        
        // Reconfigurar dificuldade
        this.configurarDificuldade(this.dificuldadeAtual);
    }

    configurarDificuldade(dificuldade) {
        const escala = this.resolutionController.getScale();
        const fatorVelocidade = 0.95 + (escala * 0.05);
        
        switch(dificuldade) {
            case 'facil':
                this.velocidadeMaxima = 0.2 * fatorVelocidade;
                this.incrementoVelocidade = 0.02 * fatorVelocidade;
                this.obstaculoController.velocidadeBase = 0.15 * fatorVelocidade;
                break;
            case 'medio':
                this.velocidadeMaxima = 0.3 * fatorVelocidade;
                this.incrementoVelocidade = 0.05 * fatorVelocidade;
                this.obstaculoController.velocidadeBase = 0.18 * fatorVelocidade;
                break;
            case 'dificil':
                this.velocidadeMaxima = 0.4 * fatorVelocidade;
                this.incrementoVelocidade = 0.08 * fatorVelocidade;
                this.obstaculoController.velocidadeBase = 0.22 * fatorVelocidade;
                break;
            case 'tutorial':
                this.velocidadeMaxima = 0.3 * fatorVelocidade;
                this.incrementoVelocidade = 0.05 * fatorVelocidade;
                this.obstaculoController.velocidadeBase = 0.18 * fatorVelocidade;
                break;
        }
        
        console.log(`🎯 Dificuldade ${dificuldade}: VelBase=${this.obstaculoController.velocidadeBase.toFixed(3)}`);
    }

    reiniciarParaMenu() {
        this.pararAumentoVelocidade();
        this.obstaculoController.parar();
        this.jogoIniciado = false;
        this.dificuldadeAtual = null;
        
        const escala = this.resolutionController.getScale();
        const fatorVelocidade = 0.95 + (escala * 0.05);
        
        this.cenario1.setVelocidade(this.velocidadeMenu1 * fatorVelocidade);
        this.cenario2.setVelocidade(this.velocidadeMenu2 * fatorVelocidade);
        
        this.timerController.pararTimer();
        this.menuController.mostrarMenu();
    }

    iniciarAumentoVelocidade() {
        this.intervaloVelocidade = setInterval(() => {
            if (this.menuController.pauseController.estaPausado()) return;
            
            const escala = this.resolutionController.getScale();
            const fatorVelocidade = 0.95 + (escala * 0.05);
            
            const novaVelocidade1 = Math.min(this.cenario1.velocidadeAlvo + (this.incrementoVelocidade * fatorVelocidade), this.velocidadeMaxima);
            const novaVelocidade2 = Math.min(this.cenario2.velocidadeAlvo + (this.incrementoVelocidade * fatorVelocidade), this.velocidadeMaxima);
            
            this.cenario1.setVelocidadeSuave(novaVelocidade1, 1000);
            this.cenario2.setVelocidadeSuave(novaVelocidade2, 1000);
            
            this.obstaculoController.atualizarVelocidade(novaVelocidade2);
            
            console.log(`📈 Velocidades aumentadas: ${novaVelocidade1.toFixed(3)} / ${novaVelocidade2.toFixed(3)}`);
        }, 60000);
    }

    pararAumentoVelocidade() {
        if (this.intervaloVelocidade) {
            clearInterval(this.intervaloVelocidade);
            this.intervaloVelocidade = null;
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    window.jogo = new Jogo();
    console.log('Jogo inicializado com sucesso');
});