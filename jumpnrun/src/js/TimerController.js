class TimerController {
    constructor() {
        this.timerContainer = document.getElementById('timer-container');
        this.timerElement = document.getElementById('timer');
        this.tempoRestante = 60;
        this.intervaloTimer = null;
        this.timerAtivo = false;
        this.jogoCongelado = false;
        this.ultimaAtualizacao = 0;
        
        console.log('TimerController inicializado');
    }

    iniciarTimer() {
        if (!this.timerContainer || !this.timerElement) {
            console.error('Elementos do timer não encontrados');
            return;
        }
        
        this.tempoRestante = 60;
        this.timerAtivo = true;
        this.mostrarTimer();
        this.atualizarDisplay();
        
        // Usar timestamp para evitar travamentos
        let ultimoTimestamp = Date.now();
        
        this.intervaloTimer = setInterval(() => {
            const agora = Date.now();
            const delta = agora - ultimoTimestamp;
            
            if (delta >= 1000) { // Só atualizar se passou 1 segundo
                ultimoTimestamp = agora;
                
                if (window.jogo && window.jogo.menuController && 
                    window.jogo.menuController.pauseController && 
                    window.jogo.menuController.pauseController.estaPausado()) {
                    return;
                }
                
                this.tempoRestante--;
                this.atualizarDisplay();
                
                if (this.tempoRestante <= 0) {
                    this.tempoEsgotado();
                }
            }
        }, 100);
        
        console.log('Timer iniciado: 60 segundos');
    }

    pararTimer() {
        this.timerAtivo = false;
        if (this.intervaloTimer) {
            clearInterval(this.intervaloTimer);
            this.intervaloTimer = null;
        }
        this.esconderTimer();
        console.log('Timer parado');
    }

    atualizarDisplay() {
        if (this.timerElement) {
            const minutos = Math.floor(this.tempoRestante / 60);
            const segundos = this.tempoRestante % 60;
            this.timerElement.textContent = `${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}`;
            
            if (this.tempoRestante <= 10) {
                this.timerElement.style.color = '#ff4444';
                this.timerElement.style.animation = 'piscar 0.5s infinite';
            } else if (this.tempoRestante <= 30) {
                this.timerElement.style.color = '#ffaa00';
                this.timerElement.style.animation = 'none';
            } else {
                this.timerElement.style.color = '#ffffff';
                this.timerElement.style.animation = 'none';
            }
        }
    }

    tempoEsgotado() {
        console.log('Tempo esgotado!');
        this.pararTimer();
        this.congelarJogo();
        
        const moedaManager = new MoedaManager();
        for (let i = 0; i < 50; i++) {
            moedaManager.adicionarMoeda();
        }
        
        const evento = new CustomEvent('moedasAtualizadas', {
            detail: { moedasTotais: moedaManager.getMoedasTotais() }
        });
        window.dispatchEvent(evento);
        
        this.mostrarMensagemCompensacao();
    }

    congelarJogo() {
        if (window.jogo && !this.jogoCongelado) {
            this.jogoCongelado = true;
            
            if (window.jogo.cenario1) {
                window.jogo.cenario1.pausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.pausar();
            }

            if (window.jogo.personagemController) {
                window.jogo.personagemController.particula.stop();
            }

            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.pausar();
            }

            if (window.jogo) {
                window.jogo.pararAumentoVelocidade();
            }

            this.desativarControles();

            console.log('Jogo congelado por tempo esgotado');
        }
    }

    descongelarJogo() {
        if (window.jogo && this.jogoCongelado) {
            this.jogoCongelado = false;
            
            if (window.jogo.cenario1) {
                window.jogo.cenario1.despausar();
            }
            if (window.jogo.cenario2) {
                window.jogo.cenario2.despausar();
            }

            if (window.jogo.personagemController) {
                window.jogo.personagemController.particula.start();
            }

            if (window.jogo.obstaculoController) {
                window.jogo.obstaculoController.despausar();
            }

            this.reativarControles();

            console.log('Jogo descongelado');
        }
    }

    mostrarMensagemCompensacao() {
        const mensagemElement = document.createElement('div');
        mensagemElement.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(10, 10, 20, 0.95);
            backdrop-filter: blur(20px);
            color: #FFFFFF;
            padding: 40px 60px;
            border-radius: 25px;
            border: 3px solid #9F7AEA;
            font-size: 28px;
            font-weight: bold;
            z-index: 10000;
            text-align: center;
            box-shadow: 
                0 0 50px rgba(159, 122, 234, 0.6),
                0 0 100px rgba(159, 122, 234, 0.3),
                inset 0 0 30px rgba(255, 255, 255, 0.1);
            font-family: 'Arial Black', sans-serif;
            line-height: 1.6;
            min-width: 500px;
            animation: aparecerSuave 0.8s ease-out;
        `;
        
        const botaoContinuar = document.createElement('div');
        botaoContinuar.style.cssText = `
            width: 300px;
            height: 100px;
            background-image: url('../assets/images/cenário/continuar.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            cursor: pointer;
            transition: transform 0.3s ease;
            margin: 30px auto 0 auto;
        `;
        
        botaoContinuar.addEventListener('mouseenter', () => {
            botaoContinuar.style.transform = 'scale(1.1)';
        });
        
        botaoContinuar.addEventListener('mouseleave', () => {
            botaoContinuar.style.transform = 'scale(1)';
        });
        
        botaoContinuar.addEventListener('click', () => {
            this.descongelarJogo();
            if (document.body.contains(mensagemElement)) {
                document.body.removeChild(mensagemElement);
            }
            if (window.jogo && window.jogo.gameOverController) {
                window.jogo.gameOverController.mostrar();
            }
        });
        
        mensagemElement.innerHTML = `
            <div style="margin-bottom: 20px; font-size: 32px; color: #9F7AEA; text-shadow: 0 0 20px rgba(159, 122, 234, 0.8);">
                TEMPO ESGOTADO
            </div>
            <div style="font-size: 24px; color: #E6E6FA; margin-bottom: 15px;">
                Sua jornada chegou ao fim
            </div>
            <div style="font-size: 26px; color: #FFD700; text-shadow: 0 0 15px rgba(255, 215, 0, 0.8);">
                +50 Moedas de Compensação
            </div>
        `;
        
        mensagemElement.appendChild(botaoContinuar);
        document.body.appendChild(mensagemElement);
        
        const style = document.createElement('style');
        style.textContent = `
            @keyframes aparecerSuave {
                0% {
                    opacity: 0;
                    transform: translate(-50%, -50%) scale(0.8);
                }
                100% {
                    opacity: 1;
                    transform: translate(-50%, -50%) scale(1);
                }
            }
        `;
        document.head.appendChild(style);
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

    mostrarTimer() {
        if (this.timerContainer) {
            this.timerContainer.classList.add('visivel');
        }
    }

    esconderTimer() {
        if (this.timerContainer) {
            this.timerContainer.classList.remove('visivel');
        }
    }

    pausarTimer() {
        console.log('Timer pausado');
    }

    despausarTimer() {
        console.log('Timer despausado');
    }
}