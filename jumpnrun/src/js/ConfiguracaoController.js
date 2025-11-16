class ConfiguracaoController {
    constructor() {
        this.menuConfiguracoes = document.getElementById('menu-configuracoes');
        this.configButton = document.getElementById('config-button');
        this.botaoVoltarConfig = document.getElementById('botao-voltar-config');
        this.botaoAddMoedas = document.getElementById('botao-add-moedas-config');
        this.botaoResetMoedas = document.getElementById('botao-reset-moedas-config');
        this.botaoResetSkins = document.getElementById('botao-reset-skins-config');
        this.botaoResetTudo = document.getElementById('botao-reset-tudo-config');
        this.moedaManager = new MoedaManager();
        this.skinManager = new SkinManager();
        
        this.botaoAumentarVelocidade = document.getElementById('botao-aumentar-velocidade');
        this.botaoDiminuirVelocidade = document.getElementById('botao-diminuir-velocidade');
        this.botaoResetVelocidade = document.getElementById('botao-reset-velocidade');
        this.indicadorVelocidade = document.getElementById('indicador-velocidade');
        
        this.velocidadeGlobal = this.carregarVelocidadeSalva();
        this.init();
    }

    carregarVelocidadeSalva() {
        const velocidadeSalva = localStorage.getItem('velocidadeGlobal');
        return velocidadeSalva ? parseFloat(velocidadeSalva) : 1.0;
    }

    salvarVelocidade() {
        localStorage.setItem('velocidadeGlobal', this.velocidadeGlobal.toString());
    }

    init() {
        if (this.configButton) {
            this.configButton.addEventListener('click', () => {
                this.mostrarMenuConfiguracoes();
            });
        }

        if (this.botaoVoltarConfig) {
            this.botaoVoltarConfig.addEventListener('click', () => {
                this.voltarParaMenuPrincipal();
            });
        }

        if (this.botaoAddMoedas) {
            this.botaoAddMoedas.addEventListener('click', () => {
                this.adicionarMoedas(10);
            });
        }

        if (this.botaoResetMoedas) {
            this.botaoResetMoedas.addEventListener('click', () => {
                this.resetarMoedas();
            });
        }

        if (this.botaoResetSkins) {
            this.botaoResetSkins.addEventListener('click', () => {
                this.resetarSkins();
            });
        }

        if (this.botaoResetTudo) {
            this.botaoResetTudo.addEventListener('click', () => {
                this.resetarTudo();
            });
        }

        if (this.botaoAumentarVelocidade) {
            this.botaoAumentarVelocidade.addEventListener('click', () => {
                this.aumentarVelocidade();
            });
        }

        if (this.botaoDiminuirVelocidade) {
            this.botaoDiminuirVelocidade.addEventListener('click', () => {
                this.diminuirVelocidade();
            });
        }

        if (this.botaoResetVelocidade) {
            this.botaoResetVelocidade.addEventListener('click', () => {
                this.resetarVelocidade();
            });
        }

        this.atualizarIndicadorVelocidade();
        this.aplicarVelocidadeGlobal();
    }

    aumentarVelocidade() {
        this.velocidadeGlobal = Math.min(10.0, this.velocidadeGlobal + 0.1);
        this.salvarVelocidade();
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem(`Velocidade: ${Math.round(this.velocidadeGlobal * 100)}%`);
    }

    diminuirVelocidade() {
        this.velocidadeGlobal = Math.max(0.5, this.velocidadeGlobal - 0.1);
        this.salvarVelocidade();
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem(`Velocidade: ${Math.round(this.velocidadeGlobal * 100)}%`);
    }

    resetarVelocidade() {
        this.velocidadeGlobal = 1.0;
        this.salvarVelocidade();
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem('Velocidade resetada para 100%');
    }

    aplicarVelocidadeGlobal() {
        if (window.jogo) {
            if (window.jogo.obstaculoController) {
                const velocidadeBaseObstaculos = 0.18;
                window.jogo.obstaculoController.atualizarVelocidade(velocidadeBaseObstaculos * this.velocidadeGlobal);
            }

            if (window.jogo.personagemController) {
                window.jogo.personagemController.atualizarVelocidadeGlobal(this.velocidadeGlobal);
            }

            if (window.jogo.timerController && window.jogo.timerController.timerAtivo) {
                window.jogo.timerController.aplicarFatorVelocidade(this.velocidadeGlobal);
            }
        }
    }

    getVelocidadeGlobal() {
        return this.velocidadeGlobal;
    }

    atualizarIndicadorVelocidade() {
        if (this.indicadorVelocidade) {
            this.indicadorVelocidade.textContent = `${Math.round(this.velocidadeGlobal * 100)}%`;
        }
    }

    mostrarMenuConfiguracoes() {
        if (this.menuConfiguracoes) {
            document.getElementById('menu-principal').style.display = 'none';
            this.menuConfiguracoes.style.display = 'flex';
            this.atualizarIndicadorVelocidade();
        }
    }

    esconderMenuConfiguracoes() {
        if (this.menuConfiguracoes) {
            this.menuConfiguracoes.style.display = 'none';
        }
    }

    voltarParaMenuPrincipal() {
        this.esconderMenuConfiguracoes();
        document.getElementById('menu-principal').style.display = 'flex';
    }

    adicionarMoedas(quantidade) {
        for (let i = 0; i < quantidade; i++) {
            this.moedaManager.adicionarMoeda();
        }
        
        if (window.jogo && window.jogo.menuController) {
            window.jogo.menuController.atualizarContadorMoedasTotal();
        }
        
        const evento = new CustomEvent('moedasAtualizadas', {
            detail: { moedasTotais: this.moedaManager.getMoedasTotais() }
        });
        window.dispatchEvent(evento);
        
        this.mostrarMensagem(`+${quantidade} moedas adicionadas!`);
    }

    resetarMoedas() {
        if (confirm('Tem certeza que deseja resetar TODAS as moedas?\nIsso irá zerar seu saldo atual.')) {
            this.moedaManager.resetarTudo();
        
            if (window.jogo && window.jogo.menuController) {
                window.jogo.menuController.atualizarContadorMoedasTotal();
            }

            const evento = new CustomEvent('moedasAtualizadas', {
                detail: { moedasTotais: this.moedaManager.getMoedasTotais() }
            });
            window.dispatchEvent(evento);
            
            this.mostrarMensagem('Moedas resetadas com sucesso!');
        }
    }

    resetarSkins() {
        if (confirm('Tem certeza que deseja resetar TODAS as skins?\nTodas as skins compradas serão perdidas.')) {
            this.skinManager.resetarProgresso();
            
            if (window.jogo && window.jogo.menuController) {
                window.jogo.menuController.carregarSkins();
            }
            
            this.mostrarMensagem('Skins resetadas com sucesso!');
        }
    }

    resetarTudo() {
        if (confirm('⚠️ ATENÇÃO ⚠️\n\nTem certeza que deseja resetar TUDO?\nIsso irá:\n• Zerar todas as moedas\n• Resetar todas as skins compradas\n• Voltar para as configurações iniciais\n\nEsta ação NÃO pode ser desfeita!')) {
            this.moedaManager.resetarTudo();
            this.skinManager.resetarProgresso();
            this.resetarVelocidade();

            if (window.jogo && window.jogo.menuController) {
                window.jogo.menuController.atualizarContadorMoedasTotal();
                window.jogo.menuController.carregarSkins();
            }
            
            const evento = new CustomEvent('moedasAtualizadas', {
                detail: { moedasTotais: this.moedaManager.getMoedasTotais() }
            });
            window.dispatchEvent(evento);
            
            this.mostrarMensagem('Jogo resetado completamente!');
        }
    }

    mostrarMensagem(mensagem) {
        const mensagemElement = document.createElement('div');
        mensagemElement.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(0, 0, 0, 0.9);
            color: white;
            padding: 20px 40px;
            border-radius: 15px;
            border: 2px solid #9F7AEA;
            font-size: 18px;
            font-weight: bold;
            z-index: 10000;
            text-align: center;
            box-shadow: 0 0 30px #9F7AEA;
        `;
        mensagemElement.textContent = mensagem;
        
        document.body.appendChild(mensagemElement);
        
        setTimeout(() => {
            document.body.removeChild(mensagemElement);
        }, 2000);
    }
}