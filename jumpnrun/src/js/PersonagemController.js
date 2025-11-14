class PersonagemController {
    constructor(personagemElement) {
        this.personagem = personagemElement;
        this.estaNoChao = true;
        this.velocidadeY = 0;
        this.gravidade = 0.6;
        this.velocidadePulo = 18;
        this.rotacao = 0;
        this.rotacaoAlvo = 0;
        this.tempoRotacao = 0;
        this.duracaoRotacao = 400;
        this.controlesAtivos = false;
        this.particula = new Particula();
        this.animacaoId = null;
        this.podePular = true;
        this.alturaChao = 130;
        this.fisicaPlataforma = new FisicaPlataforma();
        this.estaEmPlataforma = false;
        this.plataformaAtual = null;
        this.ultimaPlataforma = null;
        this.tempoForaDaPlataforma = 0;
    }

    iniciarControles() {
        if (this.controlesAtivos) return;
        
        this.controlesAtivos = true;
        this.particula.start();
        
        this.configurarEventListeners();
        this.iniciarLoopAnimacao();
    }

    configurarEventListeners() {
        this.handleKeydown = (event) => {
            if ((event.code === 'Space' || event.code === 'ArrowUp') && this.podePular) {
                this.pular();
            }
        };

        this.handleTouchstart = (event) => {
            event.preventDefault();
            if (this.podePular) {
                this.pular();
            }
        };

        document.addEventListener('keydown', this.handleKeydown);
        document.addEventListener('touchstart', this.handleTouchstart);
    }

    pular() {
        if (!this.podePular) return;
        
        if (this.estaNoChao || this.estaEmPlataforma) {
            this.estaNoChao = false;
            this.estaEmPlataforma = false;
            this.plataformaAtual = null;
            this.pulando = true;
            this.podePular = false;
            this.velocidadeY = this.velocidadePulo;
            this.rotacaoAlvo = this.rotacaoAlvo + 90;
            this.tempoRotacao = 0;
            
            setTimeout(() => {
                this.podePular = true;
            }, 200);
        }
    }

    iniciarLoopAnimacao() {
        let ultimoTempo = performance.now();
        
        const animar = (tempoAtual) => {
            if (window.jogo && window.jogo.menuController && 
                window.jogo.menuController.pauseController.estaPausado()) {
                this.animacaoId = requestAnimationFrame(animar);
                return;
            }
            
            const deltaTime = tempoAtual - ultimoTempo;
            ultimoTempo = tempoAtual;
            
            this.atualizarFisica();
            this.atualizarRotacao(deltaTime);
            this.emitirPoeira();
            this.verificarSaidaTela();
            this.animacaoId = requestAnimationFrame(animar);
        };
        
        ultimoTempo = performance.now();
        this.animacaoId = requestAnimationFrame(animar);
    }

    emitirPoeira() {
        if ((this.estaNoChao || this.estaEmPlataforma) && !this.pulando) {
            const personagemX = parseInt(this.personagem.style.left) || 250;
            const personagemBottom = parseInt(this.personagem.style.bottom) || this.alturaChao;
            
            // Passar a posição real do personagem (incluindo plataformas)
            this.particula.emitParticles(personagemX, personagemBottom);
        }
    }

    atualizarFisica() {
        const personagemX = parseInt(this.personagem.style.left) || 250;
        let bottomAtual = parseInt(this.personagem.style.bottom) || this.alturaChao;
        const personagemWidth = 83;
        const personagemHeight = 90;

        let colidindoComPlataforma = false;
        let plataformaColidida = null;

        if (window.jogo && window.jogo.obstaculoController) {
            const plataformas = window.jogo.obstaculoController.obstaculos.filter(obs => obs.tipo === 'plataforma');
            
            for (const plataforma of plataformas) {
                const colisao = this.fisicaPlataforma.verificarColisaoPlataforma(this.personagem, plataforma);
                if (colisao.porCima) {
                    colidindoComPlataforma = true;
                    plataformaColidida = plataforma;
                    break;
                }
            }
        }

        if (!this.estaNoChao && !colidindoComPlataforma) {
            this.velocidadeY -= this.gravidade;
            bottomAtual += this.velocidadeY;
            this.tempoForaDaPlataforma += 16;
        } else {
            this.tempoForaDaPlataforma = 0;
        }

        if (bottomAtual <= this.alturaChao) {
            bottomAtual = this.alturaChao;
            this.estaNoChao = true;
            this.estaEmPlataforma = false;
            this.plataformaAtual = null;
            this.pulando = false;
            this.velocidadeY = 0;
            this.podePular = true;
        }
   
        else if (colidindoComPlataforma && this.velocidadeY <= 2) {
            bottomAtual = plataformaColidida.y + plataformaColidida.height;
            this.estaNoChao = false;
            this.estaEmPlataforma = true;
            this.plataformaAtual = plataformaColidida;
            this.ultimaPlataforma = plataformaColidida;
            this.pulando = false;
            this.velocidadeY = 0;
            this.podePular = true;
        }
        else if (this.estaEmPlataforma && !colidindoComPlataforma && this.tempoForaDaPlataforma > 50) {
            this.estaEmPlataforma = false;
            this.plataformaAtual = null;
        }

        this.personagem.style.bottom = bottomAtual + 'px';
    }

    atualizarRotacao(deltaTime) {
        if (this.pulando && this.rotacao < this.rotacaoAlvo) {
            this.tempoRotacao += deltaTime;
            
            const progresso = Math.min(this.tempoRotacao / this.duracaoRotacao, 1);
            const progressoSuavizado = 1 - Math.pow(1 - progresso, 3);
            
            this.rotacao = this.rotacaoAlvo - 90 + (90 * progressoSuavizado);
            
            if (progresso >= 1) {
                this.rotacao = this.rotacaoAlvo;
            }
        }
        
        this.personagem.style.transform = `rotate(${this.rotacao}deg)`;
    }

    verificarSaidaTela() {
        const personagemX = parseInt(this.personagem.style.left) || 250;
        if (personagemX < -100) {
            this.gameOverPorSaida();
        }
    }

    gameOverPorSaida() {
        if (window.jogo && window.jogo.obstaculoController) {
            this.pararImediatamente();
            window.jogo.obstaculoController.mostrarGameOver();
        }
    }

    pararControles() {
        this.controlesAtivos = false;
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
            this.animacaoId = null;
        }
        this.particula.stop();
        
        if (this.handleKeydown) {
            document.removeEventListener('keydown', this.handleKeydown);
        }
        if (this.handleTouchstart) {
            document.removeEventListener('touchstart', this.handleTouchstart);
        }
    }

    pararImediatamente() {
        this.controlesAtivos = false;
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
            this.animacaoId = null;
        }
        this.particula.stop();
        
        if (this.handleKeydown) {
            document.removeEventListener('keydown', this.handleKeydown);
        }
        if (this.handleTouchstart) {
            document.removeEventListener('touchstart', this.handleTouchstart);
        }
    }

    resetar() {
        this.pararControles();
        this.estaNoChao = true;
        this.estaEmPlataforma = false;
        this.plataformaAtual = null;
        this.ultimaPlataforma = null;
        this.tempoForaDaPlataforma = 0;
        this.velocidadeY = 0;
        this.rotacao = 0;
        this.rotacaoAlvo = 0;
        this.tempoRotacao = 0;
        this.pulando = false;
        this.podePular = true;
        this.personagem.style.transform = 'rotate(0deg)';
        this.personagem.style.bottom = this.alturaChao + 'px';
    }
}