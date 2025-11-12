class PauseController {
    constructor() {
        this.pauseButton = document.getElementById('pause-button');
        this.menuPause = document.getElementById('menu-pause');
        this.botaoDespausar = document.getElementById('botao-despausar');
        this.botaoDesistir = document.getElementById('botao-desistir');
        this.jogoPausado = false;
        this.animacaoPersonagemId = null;
        this.init();
    }

    init() {
        this.pauseButton.addEventListener('click', () => {
            this.alternarPause();
        });

        this.botaoDespausar.addEventListener('click', () => {
            this.despausarJogo();
        });

        this.botaoDesistir.addEventListener('click', () => {
            this.desistirDoJogo();
        });
    }

    mostrarBotao() {
        this.pauseButton.style.display = 'block';
        setTimeout(() => {
            this.pauseButton.style.opacity = '1';
        }, 100);
    }

    alternarPause() {
        if (this.jogoPausado) {
            this.despausarJogo();
        } else {
            this.pausarJogo();
        }
    }

    pausarJogo() {
        this.jogoPausado = true;
        
        // Pausar cenários
        if (window.jogo && window.jogo.cenario1) {
            window.jogo.cenario1.pausar();
        }
        if (window.jogo && window.jogo.cenario2) {
            window.jogo.cenario2.pausar();
        }

        // Pausar partículas
        if (window.jogo && window.jogo.personagemController) {
            window.jogo.personagemController.particula.stop();
        }

        // Pausar obstáculos
        if (window.jogo && window.jogo.obstaculoController) {
            window.jogo.obstaculoController.pausar();
        }

        // Pausar aumento de velocidade
        if (window.jogo) {
            window.jogo.pararAumentoVelocidade();
        }

        // Pausar animação do personagem
        if (window.jogo && window.jogo.personagemController) {
            this.pararAnimacaoPersonagem();
        }

        // Pausar timer (já é pausado automaticamente pela verificação no intervalo)
        if (window.jogo && window.jogo.timerController) {
            window.jogo.timerController.pausarTimer();
        }

        // Desativar controles
        this.desativarControles();

        // Esconder botão de pause
        this.pauseButton.style.display = 'none';

        // Mostrar menu de pause
        this.menuPause.style.display = 'flex';
        
        console.log('Jogo completamente pausado');
    }

    despausarJogo() {
        this.jogoPausado = false;
        
        // Esconder menu de pause
        this.menuPause.style.display = 'none';

        // Mostrar botão de pause
        this.mostrarBotao();
        
        // Despausar cenários
        if (window.jogo && window.jogo.cenario1) {
            window.jogo.cenario1.despausar();
        }
        if (window.jogo && window.jogo.cenario2) {
            window.jogo.cenario2.despausar();
        }

        // Despausar partículas
        if (window.jogo && window.jogo.personagemController) {
            window.jogo.personagemController.particula.start();
        }

        // Despausar obstáculos
        if (window.jogo && window.jogo.obstaculoController) {
            window.jogo.obstaculoController.despausar();
        }

        // Retomar aumento de velocidade
        if (window.jogo && window.jogo.jogoIniciado) {
            window.jogo.iniciarAumentoVelocidade();
        }

        // Retomar animação do personagem
        if (window.jogo && window.jogo.personagemController) {
            this.retomarAnimacaoPersonagem();
        }

        // Despausar timer
        if (window.jogo && window.jogo.timerController) {
            window.jogo.timerController.despausarTimer();
        }

        this.reativarControles();
        
        console.log('Jogo despausado');
    }

    desistirDoJogo() {
        this.jogoPausado = false;
        
        // Esconder menu de pause
        this.menuPause.style.display = 'none';

        // Esconder botão de pause
        this.pauseButton.style.display = 'none';
        this.pauseButton.style.opacity = '0';

        // Parar tudo
        if (window.jogo) {
            // Parar aumento de velocidade
            window.jogo.pararAumentoVelocidade();
            
            // Parar obstáculos
            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.parar();
            }
            
            // Resetar cenários
            if (window.jogo.cenario1) {
                window.jogo.cenario1.setVelocidade(window.jogo.velocidadeMenu1);
                window.jogo.cenario1.despausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.setVelocidade(window.jogo.velocidadeMenu2);
                window.jogo.cenario2.despausar();
            }
            
            // Limpar obstáculos
            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.limparObstaculos();
            }
            
            // Parar timer
            if (window.jogo.timerController) {
                window.jogo.timerController.pararTimer();
            }
            
            // Resetar personagem
            const personagem = document.getElementById('personagem');
            if (personagem) {
                personagem.style.opacity = '0';
                personagem.style.left = '-200px';
                personagem.style.bottom = '130px';
                personagem.style.transform = 'rotate(0deg)';
            }
            
            if (window.jogo.personagemController) {
                window.jogo.personagemController.pararControles();
            }
            
            window.jogo.jogoIniciado = false;
        }

        // Voltar ao menu principal
        if (window.jogo && window.jogo.menuController) {
            window.jogo.menuController.mostrarMenu();
        }
        
        console.log('Jogo abandonado - Voltando ao menu principal');
    }

    pararAnimacaoPersonagem() {
        const personagemController = window.jogo.personagemController;
        
        // Salvar estado atual do personagem
        this.estadoPersonagem = {
            bottom: personagemController.personagem.style.bottom,
            transform: personagemController.personagem.style.transform,
            estaNoChao: personagemController.estaNoChao,
            velocidadeY: personagemController.velocidadeY,
            pulando: personagemController.pulando,
            rotacao: personagemController.rotacao
        };

        // Parar loop de animação
        if (personagemController.animacaoId) {
            cancelAnimationFrame(personagemController.animacaoId);
            personagemController.animacaoId = null;
        }
    }

    retomarAnimacaoPersonagem() {
        const personagemController = window.jogo.personagemController;
        
        // Restaurar estado do personagem
        if (this.estadoPersonagem) {
            personagemController.personagem.style.bottom = this.estadoPersonagem.bottom;
            personagemController.personagem.style.transform = this.estadoPersonagem.transform;
            personagemController.estaNoChao = this.estadoPersonagem.estaNoChao;
            personagemController.velocidadeY = this.estadoPersonagem.velocidadeY;
            personagemController.pulando = this.estadoPersonagem.pulando;
            personagemController.rotacao = this.estadoPersonagem.rotacao;
        }

        // Retomar loop de animação
        if (!personagemController.animacaoId) {
            personagemController.iniciarLoopAnimacao();
        }
    }

    desativarControles() {
        // Salvar controles originais
        this.controlesOriginais = {
            keydown: document.onkeydown,
            touchstart: document.ontouchstart
        };

        // Desativar controles
        document.onkeydown = null;
        document.ontouchstart = null;
    }

    reativarControles() {
        // Restaurar controles originais
        if (this.controlesOriginais) {
            document.onkeydown = this.controlesOriginais.keydown;
            document.ontouchstart = this.controlesOriginais.touchstart;
        }
    }

    estaPausado() {
        return this.jogoPausado;
    }
}