class Jogo {
    constructor() {
        this.container = document.getElementById('game-container');
        this.personagem = document.getElementById('personagem');
        
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
        this.cenario1.iniciarAnimacao();
        this.cenario2.iniciarAnimacao();
        
        window.addEventListener('resolutionChanged', (event) => {
            const fatorVelocidade = event.detail.fatorVelocidade;
            
            if (this.obstaculoController) {
                this.obstaculoController.atualizarVelocidade(this.obstaculoController.velocidadeBase);
            }
        });
    }

    iniciarJogo(dificuldade) {
        if (this.jogoIniciado) return;
        
        this.jogoIniciado = true;
        this.dificuldadeAtual = dificuldade;
        this.tempoInicio = Date.now();
        
        this.personagem.style.opacity = '1';
        this.personagem.style.left = '250px';
        
        this.cenario1.setVelocidadeSuave(this.velocidadeJogo1, 500);
        this.cenario2.setVelocidadeSuave(this.velocidadeJogo2, 500);
        
        this.configurarDificuldade(dificuldade);
        
        const velocidadeGlobal = this.configuracaoController.getVelocidadeGlobal();
        const velocidadeBaseComGlobal = this.obstaculoController.velocidadeBase * velocidadeGlobal;
        this.obstaculoController.atualizarVelocidade(velocidadeBaseComGlobal);
        
        this.obstaculoController.iniciar();
        
        this.iniciarAumentoVelocidade();
        this.personagemController.iniciarControles();
        this.timerController.iniciarTimer();
    }

    configurarDificuldade(dificuldade) {
        switch(dificuldade) {
            case 'facil':
                this.velocidadeMaxima = 0.2;
                this.incrementoVelocidade = 0.02;
                this.obstaculoController.velocidadeBase = 0.15;
                break;
            case 'medio':
                this.velocidadeMaxima = 0.3;
                this.incrementoVelocidade = 0.05;
                this.obstaculoController.velocidadeBase = 0.18;
                break;
            case 'dificil':
                this.velocidadeMaxima = 0.4;
                this.incrementoVelocidade = 0.08;
                this.obstaculoController.velocidadeBase = 0.22;
                break;
            case 'tutorial':
                this.velocidadeMaxima = 0.3;
                this.incrementoVelocidade = 0.05;
                this.obstaculoController.velocidadeBase = 0.18;
                break;
        }
    }

    reiniciarParaMenu() {
        this.pararAumentoVelocidade();
        this.obstaculoController.parar();
        this.jogoIniciado = false;
        this.dificuldadeAtual = null;
        
        this.cenario1.setVelocidade(this.velocidadeMenu1);
        this.cenario2.setVelocidade(this.velocidadeMenu2);
        
        this.timerController.pararTimer();
        this.menuController.mostrarMenu();
    }

    iniciarAumentoVelocidade() {
        this.intervaloVelocidade = setInterval(() => {
            if (this.menuController.pauseController.estaPausado()) return;
            
            const novaVelocidade1 = Math.min(this.cenario1.velocidadeAlvo + this.incrementoVelocidade, this.velocidadeMaxima);
            const novaVelocidade2 = Math.min(this.cenario2.velocidadeAlvo + this.incrementoVelocidade, this.velocidadeMaxima);
            
            this.cenario1.setVelocidadeSuave(novaVelocidade1, 1000);
            this.cenario2.setVelocidadeSuave(novaVelocidade2, 1000);
            
            const velocidadeGlobal = this.configuracaoController.getVelocidadeGlobal();
            const novaVelocidadeObstaculos = novaVelocidade2 * velocidadeGlobal;
            this.obstaculoController.atualizarVelocidade(novaVelocidadeObstaculos);
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
});