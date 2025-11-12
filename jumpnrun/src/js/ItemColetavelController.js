class ItemColetavelController {
    constructor() {
        this.container = document.getElementById('itens-coletaveis-container');
        this.contadorContainer = document.getElementById('contador-itens-container');
        this.contadorElement = document.getElementById('contador-itens');
        this.itens = [];
        this.chanceSpawn = 0.2;
        this.jogoIniciado = false;
        this.moedaManager = new MoedaManager();
        this.tutorialMoedaCriada = false;
        this.velocidadeBase = 0.18;
        this.primeiroObstaculoCriado = false;
        this.moedaTutorialCriada = false;
        this.plataformaTutorialCriada = false;
    }

    iniciar() {
        this.jogoIniciado = true;
        this.atualizarContador();
        this.mostrarContador();
        
        if (window.jogo && window.jogo.dificuldadeAtual === 'tutorial') {
            this.aguardarPlataformaTutorial();
        } else {
            this.criarMoedasEmTodasPlataformas();
        }
    }

    aguardarPlataformaTutorial() {
        const verificarPlataforma = setInterval(() => {
            if (window.jogo && window.jogo.obstaculoController && 
                window.jogo.obstaculoController.primeiraPlataformaCriada && 
                !this.moedaTutorialCriada) {
                clearInterval(verificarPlataforma);
                this.plataformaTutorialCriada = true;
                
                setTimeout(() => {
                    this.criarMoedaTutorial();
                }, 3000);
            }
        }, 100);
    }

    parar() {
        this.jogoIniciado = false;
        this.limparItens();
        this.esconderContador();
        this.tutorialMoedaCriada = false;
        this.primeiroObstaculoCriado = false;
        this.moedaTutorialCriada = false;
        this.plataformaTutorialCriada = false;
    }

    criarMoedaTutorial() {
        if (this.moedaTutorialCriada || !this.jogoIniciado) return;
        
        const moeda = document.createElement('div');
        moeda.className = 'item-coletavel';
        moeda.style.left = '1920px';
        moeda.style.bottom = '184px';
        moeda.style.opacity = '1';

        this.container.appendChild(moeda);

        const imagem = document.createElement('div');
        imagem.className = 'imagem-acima moeda-tutorial';
        imagem.style.left = '1770px';
        imagem.style.bottom = '250px';
        this.container.appendChild(imagem);

        const itemData = {
            element: moeda,
            x: 1920,
            y: 184,
            width: 30,
            height: 30,
            coletado: false,
            isTutorial: true,
            imagemAcima: imagem,
            velocidade: this.velocidadeBase
        };

        this.itens.push(itemData);
        this.moedaTutorialCriada = true;
    }

    criarMoedasEmTodasPlataformas() {
        if (!window.jogo || !window.jogo.obstaculoController) return;
        
        const plataformas = window.jogo.obstaculoController.obstaculos.filter(obs => obs.tipo === 'plataforma');
        
        plataformas.forEach(plataforma => {
            if (Math.random() < this.chanceSpawn) {
                this.criarItem(plataforma);
            }
        });
    }

    verificarPlataformasParaSpawn(plataformas) {
        if (!this.jogoIniciado) return;
        if (window.jogo && window.jogo.dificuldadeAtual === 'tutorial') return;

        plataformas.forEach(plataforma => {
            if (Math.random() < this.chanceSpawn) {
                this.criarItem(plataforma);
            }
        });
    }

    criarItem(plataforma) {
        if (window.jogo && window.jogo.dificuldadeAtual === 'tutorial') return;
        
        const itemExistente = this.itens.find(item => item.plataformaOrigem === plataforma);
        if (itemExistente) return;

        const item = document.createElement('div');
        item.className = 'item-coletavel';
        
        const x = plataforma.x + (plataforma.width / 2) - 15;
        const y = plataforma.y + plataforma.height + 10;
        
        item.style.left = `${x}px`;
        item.style.bottom = `${y}px`;

        this.container.appendChild(item);

        const itemData = {
            element: item,
            x: x,
            y: y,
            width: 30,
            height: 30,
            coletado: false,
            plataformaOrigem: plataforma
        };

        this.itens.push(itemData);
    }

    atualizarItens() {
        if (!this.jogoIniciado) return;

        for (let i = this.itens.length - 1; i >= 0; i--) {
            const item = this.itens[i];
            
            if (item.isTutorial) {
                const velocidadePixels = (item.velocidade * 1920) / 100;
                item.x -= velocidadePixels;
                item.element.style.left = `${item.x}px`;
                
                if (item.imagemAcima) {
                    item.imagemAcima.style.left = `${item.x - 150}px`;
                }
            }
            
            if (item.plataformaOrigem && !item.isTutorial) {
                const novaX = item.plataformaOrigem.x + (item.plataformaOrigem.width / 2) - 15;
                const novaY = item.plataformaOrigem.y + item.plataformaOrigem.height + 10;
                
                item.x = novaX;
                item.y = novaY;
                item.element.style.left = `${novaX}px`;
                item.element.style.bottom = `${novaY}px`;
            }

            if (item.coletado || item.x + item.width < -200) {
                if (item.imagemAcima) {
                    item.imagemAcima.remove();
                }
                item.element.remove();
                this.itens.splice(i, 1);
            }
        }
    }

    verificarColisaoComPersonagem(personagemX, personagemY, personagemWidth, personagemHeight) {
        const itensParaColetar = [];
        
        for (let i = this.itens.length - 1; i >= 0; i--) {
            const item = this.itens[i];
            
            if (!item.coletado && this.verificarColisaoRetangular(
                personagemX, personagemY, personagemWidth, personagemHeight,
                item.x, item.y, item.width, item.height
            )) {
                itensParaColetar.push(i);
            }
        }
        
        itensParaColetar.sort((a, b) => b - a).forEach(index => {
            this.coletarItem(index);
        });
    }

    verificarColisaoRetangular(x1, y1, w1, h1, x2, y2, w2, h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    coletarItem(index) {
        const item = this.itens[index];
        if (item.coletado) return;
        
        item.coletado = true;
        item.element.style.animation = 'none';
        item.element.style.transform = 'scale(2)';
        item.element.style.opacity = '0';
        
        if (item.imagemAcima) {
            item.imagemAcima.remove();
        }
        
        this.moedaManager.adicionarMoeda();
        this.atualizarContador();
        
        setTimeout(() => {
            if (this.itens[index] && this.itens[index].coletado) {
                this.itens[index].element.remove();
                this.itens.splice(index, 1);
            }
        }, 300);
    }

    atualizarContador() {
        this.contadorElement.textContent = this.moedaManager.getMoedasFaseAtual();
    }

    mostrarContador() {
        this.contadorContainer.classList.add('visivel');
    }

    esconderContador() {
        this.contadorContainer.classList.remove('visivel');
    }

    limparItens() {
        this.itens.forEach(item => {
            if (item.imagemAcima) {
                item.imagemAcima.remove();
            }
            item.element.remove();
        });
        this.itens = [];
        this.atualizarContador();
        this.tutorialMoedaCriada = false;
        this.primeiroObstaculoCriado = false;
        this.moedaTutorialCriada = false;
        this.plataformaTutorialCriada = false;
    }

    reiniciar() {
        this.limparItens();
        this.atualizarContador();
    }

    getMoedaManager() {
        return this.moedaManager;
    }

    atualizarVelocidade(novaVelocidade) {
        this.velocidadeBase = novaVelocidade;
        this.itens.forEach(item => {
            if (item.isTutorial) {
                item.velocidade = novaVelocidade;
            }
        });
    }
}