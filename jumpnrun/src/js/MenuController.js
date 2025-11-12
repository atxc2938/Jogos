class MenuController {
    constructor() {
        this.playButton = document.getElementById('play-button');
        this.configButton = document.getElementById('config-button');
        this.skinButton = document.getElementById('skin-button');
        this.menuPrincipal = document.getElementById('menu-principal');
        this.menuDificuldade = document.getElementById('menu-dificuldade');
        this.menuSkin = document.getElementById('menu-skin');
        this.facilButton = document.getElementById('facil-button');
        this.medioButton = document.getElementById('medio-button');
        this.dificilButton = document.getElementById('dificil-button');
        this.tutorialButton = document.getElementById('tutorial-button');
        this.homeButton = document.getElementById('home-button');
        this.contadorMoedasTotal = document.getElementById('contador-moedas-total');
        this.botaoVoltarSkin = document.getElementById('botao-voltar-skin');
        this.gridSkins = document.getElementById('grid-skins');
        this.pauseController = new PauseController();
        this.moedaManager = new MoedaManager();
        this.skinManager = new SkinManager();
        this.configuracaoController = new ConfiguracaoController();
        this.init();
    }

    init() {
        this.playButton.addEventListener('click', () => this.mostrarMenuDificuldade());
        this.skinButton.addEventListener('click', () => this.mostrarMenuSkin());
        this.facilButton.addEventListener('click', () => this.iniciarJogo('facil'));
        this.medioButton.addEventListener('click', () => this.iniciarJogo('medio'));
        this.dificilButton.addEventListener('click', () => this.iniciarJogo('dificil'));
        this.tutorialButton.addEventListener('click', () => this.iniciarJogo('tutorial'));
        this.homeButton.addEventListener('click', () => this.voltarParaMenuPrincipal());
        this.botaoVoltarSkin.addEventListener('click', () => this.voltarParaMenuPrincipal());
        window.addEventListener('moedasAtualizadas', () => this.atualizarContadorMoedasTotal());
        this.skinManager.aplicarSkinNoPersonagem();
    }

    mostrarMenuDificuldade() {
        this.menuPrincipal.style.display = 'none';
        this.menuDificuldade.style.display = 'flex';
        this.homeButton.style.display = 'block';
    }

    esconderMenuDificuldade() {
        this.menuDificuldade.style.display = 'none';
        this.homeButton.style.display = 'none';
    }

    mostrarMenuSkin() {
        this.menuPrincipal.style.display = 'none';
        this.menuSkin.style.display = 'flex';
        this.forcarAtualizacaoMoedas();
        this.verificarEAtualizarSkins();
    }

    esconderMenuSkin() {
        this.menuSkin.style.display = 'none';
    }

    verificarEAtualizarSkins() {
        this.skinManager.verificarNovasSkins();
        this.carregarSkins();
    }

    forcarAtualizacaoMoedas() {
        this.moedaManager.carregarMoedas();
        this.atualizarContadorMoedasTotal();
    }

    atualizarContadorMoedasTotal() {
        const moedasTotais = this.moedaManager.getMoedasTotais();
        if (this.contadorMoedasTotal) {
            this.contadorMoedasTotal.innerHTML = '';
            const iconeMoeda = document.createElement('div');
            iconeMoeda.id = 'icone-moeda-total';
            const textoMoedas = document.createElement('span');
            textoMoedas.textContent = moedasTotais;
            this.contadorMoedasTotal.appendChild(iconeMoeda);
            this.contadorMoedasTotal.appendChild(textoMoedas);
        }
    }

    carregarSkins() {
        this.gridSkins.innerHTML = '';
        const skins = this.skinManager.getSkins();
        
        skins.forEach(skin => {
            const skinElement = document.createElement('div');
            skinElement.className = 'skin-item';
            const isSelecionada = skin.id === this.skinManager.skinAtual;
            
            skinElement.innerHTML = `
                <div class="skin-imagem" style="background-image: url('${skin.imagem}')"></div>
                <div class="skin-nome">${skin.nome}</div>
                <div class="skin-preco">
                    <div class="icone-moeda-preco"></div>
                    <span>${skin.preco}</span>
                </div>
                <div class="botoes-skin-container">
                    ${!skin.comprado ? 
                        `<button class="botao-comprar" data-skin-id="${skin.id}">COMPRAR</button>` :
                        `<button class="botao-usar ${isSelecionada ? 'selecionado' : ''}" data-skin-id="${skin.id}">
                            ${isSelecionada ? 'SELECIONADO' : 'USAR'}
                        </button>`
                    }
                </div>
            `;
            
            this.gridSkins.appendChild(skinElement);
            
            if (!skin.comprado) {
                const botaoComprar = skinElement.querySelector('.botao-comprar');
                botaoComprar.addEventListener('click', () => this.comprarSkin(skin.id, botaoComprar));
            } else {
                const botaoUsar = skinElement.querySelector('.botao-usar');
                botaoUsar.addEventListener('click', () => this.usarSkin(skin.id));
            }
        });
    }

    comprarSkin(skinId, botaoElement) {
        const sucesso = this.skinManager.comprarSkin(skinId, this.moedaManager);
        
        if (sucesso) {
            this.forcarAtualizacaoMoedas();
            this.carregarSkins();
            const evento = new CustomEvent('moedasAtualizadas', {
                detail: { moedasTotais: this.moedaManager.getMoedasTotais() }
            });
            window.dispatchEvent(evento);
        } else {
            botaoElement.classList.add('insuficiente');
            setTimeout(() => botaoElement.classList.remove('insuficiente'), 500);
        }
    }

    usarSkin(skinId) {
        this.skinManager.selecionarSkin(skinId);
        this.carregarSkins();
    }

    iniciarJogo(dificuldade) {
        this.esconderMenuDificuldade();
        const menuPause = document.getElementById('menu-pause');
        menuPause.style.display = 'none';
        const personagem = document.getElementById('personagem');
        personagem.style.opacity = '1';
        personagem.style.left = '250px';
        if (window.jogo) window.jogo.iniciarJogo(dificuldade);
        this.pauseController.mostrarBotao();
    }

    voltarParaMenuPrincipal() {
        this.esconderMenuDificuldade();
        this.esconderMenuSkin();
        this.mostrarMenu();
    }

    mostrarMenu() {
        this.menuPrincipal.style.display = 'flex';
        this.menuDificuldade.style.display = 'none';
        this.menuSkin.style.display = 'none';
        this.homeButton.style.display = 'none';
        const pauseButton = document.getElementById('pause-button');
        pauseButton.style.display = 'none';
        pauseButton.style.opacity = '0';
        const menuPause = document.getElementById('menu-pause');
        menuPause.style.display = 'none';
        const personagem = document.getElementById('personagem');
        personagem.style.opacity = '0';
        personagem.style.left = '-200px';
        const telaGameOver = document.getElementById('tela-game-over');
        telaGameOver.style.display = 'none';
        const contadorItens = document.getElementById('contador-itens-container');
        contadorItens.classList.remove('visivel');
        // Esconder timer
        const timerContainer = document.getElementById('timer-container');
        if (timerContainer) {
            timerContainer.classList.remove('visivel');
        }
    }
}