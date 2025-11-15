class ObstaculoController {
    constructor() {
        this.container = document.getElementById('obstaculos-container');
        this.obstaculos = [];
        this.velocidadeBase = 0.18;
        this.jogoIniciado = false;
        this.animacaoId = null;
        this.debugAtivado = false;
        this.primeiroEspinhoCriado = false;
        this.primeiraPlataformaCriada = false;
        this.intervaloGeracao = null;
        this.geradorObstaculos = new GeradorObstaculos();
        this.ultimoXObstaculo = 1920;
        this.itemColetavelController = new ItemColetavelController();
    }

    iniciar() {
        this.jogoIniciado = true;
        this.ultimoXObstaculo = 1920;
        this.itemColetavelController.iniciar();
        
        if (window.jogo && window.jogo.dificuldadeAtual === 'tutorial') {
            setTimeout(() => {
                this.criarEspinho();
            }, 1000);

            setTimeout(() => {
                this.criarPlataforma();
            }, 4000);
        } else {
            this.iniciarGeracaoProcedural();
        }

        this.iniciarAnimacao();
    }

    iniciarGeracaoProcedural() {
        const gerarProcedural = () => {
            if (!this.jogoIniciado) return;

            const config = this.geradorObstaculos.gerarProximoPadrao();
            this.criarPadraoObstaculos(config);

            let intervaloBase;
            switch(window.jogo.dificuldadeAtual) {
                case 'facil':
                    intervaloBase = 1500;
                    break;
                case 'medio':
                    intervaloBase = 1100;
                    break;
                case 'dificil':
                    intervaloBase = 700;
                    break;
                default:
                    intervaloBase = 1100;
            }

            const fatorVelocidade = Math.max(0.5, 1 - (this.velocidadeBase - 0.18) / 0.3);
            const intervalo = intervaloBase * fatorVelocidade;

            setTimeout(gerarProcedural, intervalo);
        };

        let delayInicial;
        switch(window.jogo.dificuldadeAtual) {
            case 'facil':
                delayInicial = 1000;
                break;
            case 'medio':
                delayInicial = 800;
                break;
            case 'dificil':
                delayInicial = 600;
                break;
            default:
                delayInicial = 800;
        }

        setTimeout(gerarProcedural, delayInicial);
    }

    criarPadraoObstaculos(config) {
        const { tipo, sequenciaAlturas, quantidade, espacamento, comEspinhos } = config;
        
        if (tipo === 'plataforma_escada') {
            this.criarEscadaPlataformas(sequenciaAlturas, espacamento, comEspinhos);
        } else if (tipo === 'espinho') {
            this.criarGrupoEspinhos(quantidade, espacamento);
        } else if (tipo === 'misto_escada') {
            this.criarSequenciaMistaEscalonada(sequenciaAlturas, quantidade, espacamento, comEspinhos);
        }
    }

    criarEscadaPlataformas(sequenciaAlturas, espacamento, comEspinhos) {
        let xInicial = this.ultimoXObstaculo + 200;
        
        for (let i = 0; i < sequenciaAlturas.length; i++) {
            const altura = sequenciaAlturas[i];
            const x = xInicial + (i * espacamento);
            const y = this.geradorObstaculos.calcularYParaAltura(altura);
            
            this.criarPlataforma(x, y);
        }
        
        if (comEspinhos && window.jogo.dificuldadeAtual !== 'facil') {
            this.criarEspinhosParaSequencia(sequenciaAlturas, xInicial, espacamento);
        }
        
        this.ultimoXObstaculo = xInicial + (sequenciaAlturas.length * espacamento);
    }

    criarEspinhosParaSequencia(sequenciaAlturas, xInicial, espacamento) {
        const temPlataformasAltas = sequenciaAlturas.some(altura => altura > 1);
        
        if (!temPlataformasAltas) return;
        
        this.criarEspinhosDebaixoDeTodas(sequenciaAlturas, xInicial, espacamento);
        this.criarEspinhosEntreTodas(sequenciaAlturas, xInicial, espacamento);
        this.criarEspinhoAposUltima(sequenciaAlturas, xInicial, espacamento);
    }

    criarEspinhosDebaixoDeTodas(sequenciaAlturas, xInicial, espacamento) {
        for (let i = 0; i < sequenciaAlturas.length; i++) {
            const altura = sequenciaAlturas[i];
            if (altura > 1) {
                const xPlataforma = xInicial + (i * espacamento);
                this.criarEspinhosDebaixoDaPlataforma(xPlataforma, altura);
            }
        }
    }

    criarEspinhosDebaixoDaPlataforma(xPlataforma, altura) {
        const larguraPlataforma = 80;
        const larguraEspinho = 60;
        const espacamentoEntreEspinhos = 10;
        
        let quantidadeEspinhos;
        switch(window.jogo.dificuldadeAtual) {
            case 'facil':
                quantidadeEspinhos = 1;
                break;
            case 'medio':
                quantidadeEspinhos = Math.min(2, altura);
                break;
            case 'dificil':
                quantidadeEspinhos = Math.min(3, altura);
                break;
            default:
                quantidadeEspinhos = Math.min(2, altura);
        }
        
        const larguraTotal = (quantidadeEspinhos * larguraEspinho) + ((quantidadeEspinhos - 1) * espacamentoEntreEspinhos);
        const xInicial = xPlataforma + (larguraPlataforma - larguraTotal) / 2;
        
        for (let i = 0; i < quantidadeEspinhos; i++) {
            const xEspinho = xInicial + (i * (larguraEspinho + espacamentoEntreEspinhos));
            if (!this.verificarSobreposicao(xEspinho, 134, larguraEspinho, larguraEspinho)) {
                this.criarEspinho(xEspinho);
            }
        }
    }

    criarEspinhosEntreTodas(sequenciaAlturas, xInicial, espacamento) {
        for (let i = 1; i < sequenciaAlturas.length; i++) {
            const xEntre = xInicial + (i * espacamento) - (espacamento / 2);
            if (!this.verificarSobreposicao(xEntre, 134, 60, 60)) {
                this.criarEspinho(xEntre);
            }
        }
    }

    criarEspinhoAposUltima(sequenciaAlturas, xInicial, espacamento) {
        const xApos = xInicial + (sequenciaAlturas.length * espacamento) + 40;
        if (!this.verificarSobreposicao(xApos, 134, 60, 60)) {
            this.criarEspinho(xApos);
        }
    }

    criarGrupoEspinhos(quantidade, espacamento) {
        let xInicial = this.ultimoXObstaculo + 200;
        
        for (let i = 0; i < quantidade; i++) {
            const x = xInicial + (i * espacamento);
            this.criarEspinho(x);
        }
        
        this.ultimoXObstaculo = xInicial + (quantidade * espacamento);
    }

    criarSequenciaMistaEscalonada(sequenciaAlturas, quantidade, espacamento, comEspinhos) {
        let xInicial = this.ultimoXObstaculo + 200;
        
        for (let i = 0; i < quantidade; i++) {
            const x = xInicial + (i * espacamento);
            
            if (i < sequenciaAlturas.length) {
                const altura = sequenciaAlturas[i];
                const y = this.geradorObstaculos.calcularYParaAltura(altura);
                this.criarPlataforma(x, y);
            } else {
                this.criarEspinho(x);
            }
        }
        
        if (comEspinhos && window.jogo.dificuldadeAtual !== 'facil') {
            this.criarEspinhosParaSequencia(sequenciaAlturas, xInicial, espacamento);
        }
        
        this.ultimoXObstaculo = xInicial + (quantidade * espacamento);
    }

    criarEspinho(x = 1920) {
        if (!this.jogoIniciado) return;

        if (this.verificarSobreposicao(x, 130, 60, 60)) {
            x += 100;
        }

        const espinho = document.createElement('div');
        espinho.className = 'obstaculo espinho';
        espinho.style.left = `${x}px`;
        espinho.style.bottom = '134px';

        const hitboxTriangular = document.createElement('div');
        hitboxTriangular.className = 'hitbox-triangular';
        hitboxTriangular.style.left = `${x}px`;
        hitboxTriangular.style.bottom = '134px';
        hitboxTriangular.style.width = '60px';
        hitboxTriangular.style.height = '60px';

        this.container.appendChild(espinho);
        this.container.appendChild(hitboxTriangular);

        const obstaculoData = {
            element: espinho,
            hitboxElement: hitboxTriangular,
            tipo: 'espinho',
            x: x,
            y: 130,
            width: 60,
            height: 60,
            velocidade: this.velocidadeBase,
            pontosTriangulo: [
                { x: x + 30, y: 130 + 60 },
                { x: x, y: 130 },
                { x: x + 60, y: 130 }
            ]
        };

        if (!this.primeiroEspinhoCriado && window.jogo && window.jogo.dificuldadeAtual === 'tutorial') {
            this.primeiroEspinhoCriado = true;
            this.criarImagemAcima(obstaculoData, 'isso_te_mata');
        }

        this.obstaculos.push(obstaculoData);
        return x;
    }

    criarPlataforma(x = 1920, y = 134) {
        if (!this.jogoIniciado) return;

        if (this.verificarSobreposicao(x, y, 80, 80)) {
            x += 120;
        }

        const plataforma = document.createElement('div');
        plataforma.className = 'obstaculo plataforma';
        plataforma.style.left = `${x}px`;
        plataforma.style.bottom = `${y}px`;

        this.container.appendChild(plataforma);

        const obstaculoData = {
            element: plataforma,
            tipo: 'plataforma',
            x: x,
            y: y,
            width: 80,
            height: 80,
            velocidade: this.velocidadeBase
        };

        if (!this.primeiraPlataformaCriada && window.jogo && window.jogo.dificuldadeAtual === 'tutorial') {
            this.primeiraPlataformaCriada = true;
            this.criarImagemAcima(obstaculoData, 'isso_te_ajuda');
        }

        this.obstaculos.push(obstaculoData);

        if (this.jogoIniciado && window.jogo && window.jogo.dificuldadeAtual !== 'tutorial') {
            this.itemColetavelController.verificarPlataformasParaSpawn([obstaculoData]);
        }

        return x;
    }

    verificarSobreposicao(x, y, width, height) {
        for (const obstaculo of this.obstaculos) {
            if (this.verificarColisaoRetangular(x, y, width, height, obstaculo.x, obstaculo.y, obstaculo.width, obstaculo.height)) {
                return true;
            }
        }
        return false;
    }

    verificarColisaoRetangular(x1, y1, w1, h1, x2, y2, w2, h2) {
        return x1 < x2 + w2 &&
               x1 + w1 > x2 &&
               y1 < y2 + h2 &&
               y1 + h1 > y2;
    }

    criarImagemAcima(obstaculoData, nomeImagem) {
        const imagem = document.createElement('div');
        imagem.className = `imagem-acima ${nomeImagem}`;
        
        if (obstaculoData.tipo === 'plataforma') {
            imagem.style.left = `${obstaculoData.x - 150}px`;
        } else {
            imagem.style.left = `${obstaculoData.x - 150}px`;
        }
        imagem.style.bottom = `${obstaculoData.y + obstaculoData.height + 40}px`;

        this.container.appendChild(imagem);
        obstaculoData.imagemAcima = imagem;
    }

    iniciarAnimacao() {
        const animar = () => {
            if (!this.jogoIniciado) return;

            this.atualizarObstaculos();
            this.verificarColisoes();
            this.animacaoId = requestAnimationFrame(animar);
        };

        this.animacaoId = requestAnimationFrame(animar);
    }

    atualizarObstaculos() {
        for (let i = this.obstaculos.length - 1; i >= 0; i--) {
            const obstaculo = this.obstaculos[i];
            
            const velocidadePixels = (obstaculo.velocidade * 1920) / 100;
            obstaculo.x -= velocidadePixels;
            obstaculo.element.style.left = `${obstaculo.x}px`;

            if (obstaculo.tipo === 'espinho' && obstaculo.hitboxElement) {
                obstaculo.hitboxElement.style.left = `${obstaculo.x}px`;
            }

            if (obstaculo.imagemAcima) {
                if (obstaculo.tipo === 'plataforma') {
                    obstaculo.imagemAcima.style.left = `${obstaculo.x - 150}px`;
                } else {
                    obstaculo.imagemAcima.style.left = `${obstaculo.x - 150}px`;
                }
                obstaculo.imagemAcima.style.bottom = `${obstaculo.y + obstaculo.height + 40}px`;
            }

            if (obstaculo.tipo === 'espinho' && obstaculo.pontosTriangulo) {
                obstaculo.pontosTriangulo = [
                    { x: obstaculo.x + obstaculo.width / 2, y: obstaculo.y + obstaculo.height },
                    { x: obstaculo.x, y: obstaculo.y },
                    { x: obstaculo.x + obstaculo.width, y: obstaculo.y }
                ];
            }

            if (obstaculo.x + obstaculo.width < 0) {
                obstaculo.element.remove();
                if (obstaculo.hitboxElement) {
                    obstaculo.hitboxElement.remove();
                }
                if (obstaculo.imagemAcima) {
                    obstaculo.imagemAcima.remove();
                }
                this.obstaculos.splice(i, 1);
            }
        }

        this.itemColetavelController.atualizarItens();
    }

    verificarColisoes() {
        const personagem = document.getElementById('personagem');
        if (!personagem) return;

        const container = document.getElementById('game-container');
        const scaleMatch = container.style.transform.match(/scale\(([^)]+)\)/);
        const scale = scaleMatch ? parseFloat(scaleMatch[1]) : 1;

        for (const obstaculo of this.obstaculos) {
            if (obstaculo.tipo === 'espinho') {
                const colidiu = this.verificarColisaoTriangularComPersonagem(personagem, obstaculo, scale);
                if (colidiu) {
                    this.matarPersonagem();
                    return;
                }
            }
        }

        const personagemRect = personagem.getBoundingClientRect();
        const containerRect = this.container.getBoundingClientRect();

        const personagemX = (personagemRect.left - containerRect.left) / scale;
        const personagemY = (containerRect.bottom - personagemRect.bottom) / scale;
        const personagemWidth = personagemRect.width / scale;
        const personagemHeight = personagemRect.height / scale;

        this.itemColetavelController.verificarColisaoComPersonagem(
            personagemX, personagemY, personagemWidth, personagemHeight, scale
        );
    }

    verificarColisaoTriangularComPersonagem(personagem, obstaculo, scale) {
        const personagemRect = personagem.getBoundingClientRect();
        const containerRect = this.container.getBoundingClientRect();

        const personagemX = (personagemRect.left - containerRect.left) / scale;
        const personagemY = (containerRect.bottom - personagemRect.bottom) / scale;
        const personagemWidth = personagemRect.width / scale;
        const personagemHeight = personagemRect.height / scale;

        return this.verificarColisaoTriangular(
            personagemX, personagemY, personagemWidth, personagemHeight,
            obstaculo.pontosTriangulo
        );
    }

    verificarColisaoTriangular(personagemX, personagemY, personagemWidth, personagemHeight, pontosTriangulo) {
        if (!pontosTriangulo || pontosTriangulo.length !== 3) return false;

        const personagemPontos = [
            { x: personagemX, y: personagemY },
            { x: personagemX + personagemWidth, y: personagemY },
            { x: personagemX, y: personagemY + personagemHeight },
            { x: personagemX + personagemWidth, y: personagemY + personagemHeight }
        ];

        for (const ponto of personagemPontos) {
            if (this.pontoDentroDoTriangulo(ponto, pontosTriangulo)) {
                return true;
            }
        }

        for (const ponto of pontosTriangulo) {
            if (this.pontoDentroDoRetangulo(ponto, personagemX, personagemY, personagemWidth, personagemHeight)) {
                return true;
            }
        }

        return false;
    }

    pontoDentroDoTriangulo(ponto, triangulo) {
        const [A, B, C] = triangulo;
        
        const areaTotal = this.areaTriangulo(A, B, C);
        const area1 = this.areaTriangulo(ponto, B, C);
        const area2 = this.areaTriangulo(A, ponto, C);
        const area3 = this.areaTriangulo(A, B, ponto);
        
        return Math.abs(areaTotal - (area1 + area2 + area3)) < 0.1;
    }

    areaTriangulo(A, B, C) {
        return Math.abs(
            (A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y)) / 2
        );
    }

    pontoDentroDoRetangulo(ponto, rectX, rectY, rectWidth, rectHeight) {
        return ponto.x >= rectX && 
               ponto.x <= rectX + rectWidth && 
               ponto.y >= rectY && 
               ponto.y <= rectY + rectHeight;
    }

    matarPersonagem() {
        if (window.jogo && window.jogo.gameOverController) {
            window.jogo.gameOverController.mostrar();
        }
        this.parar();
    }

    parar() {
        this.jogoIniciado = false;
        this.itemColetavelController.parar();
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
            this.animacaoId = null;
        }
        if (this.intervaloGeracao) {
            clearTimeout(this.intervaloGeracao);
            this.intervaloGeracao = null;
        }
    }

    pararImediatamente() {
        this.jogoIniciado = false;
        this.itemColetavelController.parar();
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
            this.animacaoId = null;
        }
        if (this.intervaloGeracao) {
            clearTimeout(this.intervaloGeracao);
            this.intervaloGeracao = null;
        }
    }

    mostrarGameOver() {
        if (window.jogo && window.jogo.gameOverController) {
            window.jogo.gameOverController.mostrar();
        }
    }

    limparObstaculos() {
        for (const obstaculo of this.obstaculos) {
            obstaculo.element.remove();
            if (obstaculo.hitboxElement) {
                obstaculo.hitboxElement.remove();
            }
            if (obstaculo.imagemAcima) {
                obstaculo.imagemAcima.remove();
            }
        }
        this.obstaculos = [];
        this.primeiroEspinhoCriada = false;
        this.primeiraPlataformaCriada = false;
        this.ultimoXObstaculo = 1920;
        this.geradorObstaculos.reiniciar();
        this.itemColetavelController.limparItens();
    }

    pausar() {
        this.jogoIniciado = false;
        this.itemColetavelController.parar();
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
        }
        if (this.intervaloGeracao) {
            clearTimeout(this.intervaloGeracao);
        }
    }

    despausar() {
        this.jogoIniciado = true;
        this.itemColetavelController.iniciar();
        this.iniciarAnimacao();
        
        if (window.jogo && window.jogo.dificuldadeAtual !== 'tutorial') {
            this.iniciarGeracaoProcedural();
        }
    }

    atualizarVelocidade(novaVelocidade) {
        this.velocidadeBase = novaVelocidade;
        this.geradorObstaculos.atualizarVelocidade(novaVelocidade);
        this.itemColetavelController.atualizarVelocidade(novaVelocidade);
        
        this.obstaculos.forEach(obstaculo => {
            obstaculo.velocidade = this.velocidadeBase;
        });
    }

    configurarHitbox(tipo, configuracao) {
        const obstaculos = this.obstaculos.filter(obs => obs.tipo === tipo);
        
        obstaculos.forEach(obstaculo => {
            obstaculo.width = configuracao.width || obstaculo.width;
            obstaculo.height = configuracao.height || obstaculo.height;
            obstaculo.x = configuracao.x !== undefined ? configuracao.x : obstaculo.x;
            obstaculo.y = configuracao.y !== undefined ? configuracao.y : obstaculo.y;
            
            obstaculo.element.style.width = `${obstaculo.width}px`;
            obstaculo.element.style.height = `${obstaculo.height}px`;
            obstaculo.element.style.left = `${obstaculo.x}px`;
            obstaculo.element.style.bottom = `${obstaculo.y}px`;
            
            if (tipo === 'espinho' && obstaculo.hitboxElement) {
                obstaculo.hitboxElement.style.width = `${obstaculo.width}px`;
                obstaculo.hitboxElement.style.height = `${obstaculo.height}px`;
                obstaculo.hitboxElement.style.left = `${obstaculo.x}px`;
                obstaculo.hitboxElement.style.bottom = `${obstaculo.y}px`;
            }
            
            if (tipo === 'espinho' && obstaculo.pontosTriangulo) {
                obstaculo.pontosTriangulo = [
                    { x: obstaculo.x + obstaculo.width / 2, y: obstaculo.y + obstaculo.height },
                    { x: obstaculo.x, y: obstaculo.y },
                    { x: obstaculo.x + obstaculo.width, y: obstaculo.y }
                ];
            }
            
            if (configuracao.velocidade !== undefined) {
                obstaculo.velocidade = configuracao.velocidade;
            }
        });
    }

    toggleDebugHitbox(ativar = true) {
        this.debugAtivado = ativar;
        
        const todosObstaculos = document.querySelectorAll('.obstaculo');
        const todasHitboxes = document.querySelectorAll('.hitbox-triangular');
        
        todosObstaculos.forEach(obstaculo => {
            if (obstaculo.classList.contains('plataforma')) {
                if (ativar) {
                    obstaculo.classList.add('debug');
                } else {
                    obstaculo.classList.remove('debug');
                }
            }
        });
        
        todasHitboxes.forEach(hitbox => {
            if (ativar) {
                hitbox.classList.add('debug');
            } else {
                hitbox.classList.remove('debug');
            }
        });
    }
}