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
        
   
        if (window.jogo && window.jogo.cenario1) {
            window.jogo.cenario1.pausar();
        }
        if (window.jogo && window.jogo.cenario2) {
            window.jogo.cenario2.pausar();
        }

 
        if (window.jogo && window.jogo.personagemController) {
            window.jogo.personagemController.particula.stop();
        }


        if (window.jogo && window.jogo.obstaculoController) {
            window.jogo.obstaculoController.pausar();
        }

      
        if (window.jogo) {
            window.jogo.pararAumentoVelocidade();
        }

        if (window.jogo && window.jogo.personagemController) {
            this.pararAnimacaoPersonagem();
        }


        this.desativarControles();

     
        this.pauseButton.style.display = 'none';

     
        this.menuPause.style.display = 'flex';
        
        console.log('Jogo completamente pausado');
    }

    despausarJogo() {
        this.jogoPausado = false;
        
      
        this.menuPause.style.display = 'none';

       
        this.mostrarBotao();
      
        if (window.jogo && window.jogo.cenario1) {
            window.jogo.cenario1.despausar();
        }
        if (window.jogo && window.jogo.cenario2) {
            window.jogo.cenario2.despausar();
        }

    
        if (window.jogo && window.jogo.personagemController) {
            window.jogo.personagemController.particula.start();
        }

    
        if (window.jogo && window.jogo.obstaculoController) {
            window.jogo.obstaculoController.despausar();
        }

        if (window.jogo && window.jogo.jogoIniciado) {
            window.jogo.iniciarAumentoVelocidade();
        }

     
        if (window.jogo && window.jogo.personagemController) {
            this.retomarAnimacaoPersonagem();
        }

        this.reativarControles();
        
        console.log('Jogo despausado');
    }

    desistirDoJogo() {
        this.jogoPausado = false;
        
  
        this.menuPause.style.display = 'none';


        this.pauseButton.style.display = 'none';
        this.pauseButton.style.opacity = '0';


        if (window.jogo) {

            window.jogo.pararAumentoVelocidade();
            

            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.parar();
            }
            

            if (window.jogo.cenario1) {
                window.jogo.cenario1.setVelocidade(window.jogo.velocidadeMenu1);
                window.jogo.cenario1.despausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.setVelocidade(window.jogo.velocidadeMenu2);
                window.jogo.cenario2.despausar();
            }
            

            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.limparObstaculos();
            }
            

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


        if (window.jogo && window.jogo.menuController) {
            window.jogo.menuController.mostrarMenu();
        }
        
        console.log('Jogo abandonado - Voltando ao menu principal');
    }

    pararAnimacaoPersonagem() {
        const personagemController = window.jogo.personagemController;
        

        this.estadoPersonagem = {
            bottom: personagemController.personagem.style.bottom,
            transform: personagemController.personagem.style.transform,
            estaNoChao: personagemController.estaNoChao,
            velocidadeY: personagemController.velocidadeY,
            pulando: personagemController.pulando,
            rotacao: personagemController.rotacao
        };


        if (personagemController.animacaoId) {
            cancelAnimationFrame(personagemController.animacaoId);
            personagemController.animacaoId = null;
        }
    }

    retomarAnimacaoPersonagem() {
        const personagemController = window.jogo.personagemController;
        

        if (this.estadoPersonagem) {
            personagemController.personagem.style.bottom = this.estadoPersonagem.bottom;
            personagemController.personagem.style.transform = this.estadoPersonagem.transform;
            personagemController.estaNoChao = this.estadoPersonagem.estaNoChao;
            personagemController.velocidadeY = this.estadoPersonagem.velocidadeY;
            personagemController.pulando = this.estadoPersonagem.pulando;
            personagemController.rotacao = this.estadoPersonagem.rotacao;
        }

        if (!personagemController.animacaoId) {
            personagemController.iniciarLoopAnimacao();
        }
    }

    desativarControles() {

        this.controlesOriginais = {
            keydown: document.onkeydown,
            touchstart: document.ontouchstart
        };

        document.onkeydown = null;
        document.ontouchstart = null;
    }

    reativarControles() {

        if (this.controlesOriginais) {
            document.onkeydown = this.controlesOriginais.keydown;
            document.ontouchstart = this.controlesOriginais.touchstart;
        }
    }

    estaPausado() {
        return this.jogoPausado;
    }
}