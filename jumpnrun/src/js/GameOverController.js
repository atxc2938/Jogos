class GameOverController {
    constructor() {
        this.telaGameOver = document.getElementById('tela-game-over');
        this.botaoContinuar = document.getElementById('botao-continuar');
        this.init();
    }

    init() {
        this.botaoContinuar.addEventListener('click', () => {
            this.voltarParaMenu();
        });
    }

    mostrar() {
        this.congelarJogo();
        
        this.telaGameOver.style.display = 'flex';
    }

    esconder() {
        this.telaGameOver.style.display = 'none';
    }

    congelarJogo() {
        if (window.jogo) {
            window.jogo.pararAumentoVelocidade();
            
            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.parar();
            }
            
            if (window.jogo.cenario1) {
                window.jogo.cenario1.pausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.pausar();
            }
            
            if (window.jogo.personagemController && window.jogo.personagemController.particula) {
                window.jogo.personagemController.particula.stop();
            }
            
            if (window.jogo.personagemController) {
                window.jogo.personagemController.pararControles();
            }
            
            window.jogo.jogoIniciado = false;
        }
    }

    descongelarJogo() {
        if (window.jogo) {
            if (window.jogo.cenario1) {
                window.jogo.cenario1.despausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.despausar();
            }
            
            window.jogo.cenario1.setVelocidade(window.jogo.velocidadeMenu1);
            window.jogo.cenario2.setVelocidade(window.jogo.velocidadeMenu2);
        }
    }

    voltarParaMenu() {
        this.descongelarJogo();
        this.esconder();
        
        // Resetar personagem
        const personagem = document.getElementById('personagem');
        if (personagem) {
            personagem.style.opacity = '0';
            personagem.style.left = '-200px';
            personagem.style.bottom = '130px';
            personagem.style.transform = 'rotate(0deg)';
        }
        
        // Limpar obstáculos
        if (window.jogo && window.jogo.obstaculoController) {
            window.jogo.obstaculoController.limparObstaculos();
        }
        
        // Mostrar menu principal
        if (window.jogo && window.jogo.menuController) {
            window.jogo.menuController.mostrarMenu();
        }
    }
}