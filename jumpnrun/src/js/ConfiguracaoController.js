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
        this.init();
    }

    init() {
        // Botão Configurações no menu principal
        this.configButton.addEventListener('click', () => {
            this.mostrarMenuConfiguracoes();
        });

        // Botão Voltar
        this.botaoVoltarConfig.addEventListener('click', () => {
            this.voltarParaMenuPrincipal();
        });

        // Botão Adicionar Moedas
        this.botaoAddMoedas.addEventListener('click', () => {
            this.adicionarMoedas(10);
        });

        // Botão Resetar Moedas
        this.botaoResetMoedas.addEventListener('click', () => {
            this.resetarMoedas();
        });

        // Botão Resetar Skins
        this.botaoResetSkins.addEventListener('click', () => {
            this.resetarSkins();
        });

        // Botão Resetar Tudo
        this.botaoResetTudo.addEventListener('click', () => {
            this.resetarTudo();
        });
    }

    mostrarMenuConfiguracoes() {
        document.getElementById('menu-principal').style.display = 'none';
        this.menuConfiguracoes.style.display = 'flex';
    }

    esconderMenuConfiguracoes() {
        this.menuConfiguracoes.style.display = 'none';
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