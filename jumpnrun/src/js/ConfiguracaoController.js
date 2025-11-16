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
        
        // NOVOS BOTÕES DE VELOCIDADE (com verificação de null)
        this.botaoAumentarVelocidade = document.getElementById('botao-aumentar-velocidade');
        this.botaoDiminuirVelocidade = document.getElementById('botao-diminuir-velocidade');
        this.botaoResetVelocidade = document.getElementById('botao-reset-velocidade');
        this.indicadorVelocidade = document.getElementById('indicador-velocidade');
        
        // NOVO: Carregar velocidade salva
        this.velocidadeGlobal = this.carregarVelocidadeSalva();
        this.init();
    }

    // NOVO: Método para carregar velocidade salva
    carregarVelocidadeSalva() {
        const velocidadeSalva = localStorage.getItem('velocidadeGlobal');
        return velocidadeSalva ? parseFloat(velocidadeSalva) : 1.0;
    }

    // NOVO: Método para salvar velocidade
    salvarVelocidade() {
        localStorage.setItem('velocidadeGlobal', this.velocidadeGlobal.toString());
    }

    init() {
        // Botão Configurações no menu principal
        if (this.configButton) {
            this.configButton.addEventListener('click', () => {
                this.mostrarMenuConfiguracoes();
            });
        }

        // Botão Voltar
        if (this.botaoVoltarConfig) {
            this.botaoVoltarConfig.addEventListener('click', () => {
                this.voltarParaMenuPrincipal();
            });
        }

        // Botão Adicionar Moedas
        if (this.botaoAddMoedas) {
            this.botaoAddMoedas.addEventListener('click', () => {
                this.adicionarMoedas(10);
            });
        }

        // Botão Resetar Moedas
        if (this.botaoResetMoedas) {
            this.botaoResetMoedas.addEventListener('click', () => {
                this.resetarMoedas();
            });
        }

        // Botão Resetar Skins
        if (this.botaoResetSkins) {
            this.botaoResetSkins.addEventListener('click', () => {
                this.resetarSkins();
            });
        }

        // Botão Resetar Tudo
        if (this.botaoResetTudo) {
            this.botaoResetTudo.addEventListener('click', () => {
                this.resetarTudo();
            });
        }

        // NOVOS: Botões de controle de velocidade (com verificação de null)
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
        
        // NOVO: Aplicar velocidade salva ao iniciar
        this.aplicarVelocidadeGlobal();
    }

    // NOVOS MÉTODOS PARA CONTROLE DE VELOCIDADE
    aumentarVelocidade() {
        this.velocidadeGlobal = Math.min(2.0, this.velocidadeGlobal + 0.1); // Máximo 200%
        this.salvarVelocidade(); // NOVO: Salvar velocidade
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem(`Velocidade: ${Math.round(this.velocidadeGlobal * 100)}%`);
    }

    diminuirVelocidade() {
        this.velocidadeGlobal = Math.max(0.5, this.velocidadeGlobal - 0.1); // Mínimo 50%
        this.salvarVelocidade(); // NOVO: Salvar velocidade
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem(`Velocidade: ${Math.round(this.velocidadeGlobal * 100)}%`);
    }

    resetarVelocidade() {
        this.velocidadeGlobal = 1.0;
        this.salvarVelocidade(); // NOVO: Salvar velocidade
        this.aplicarVelocidadeGlobal();
        this.atualizarIndicadorVelocidade();
        this.mostrarMensagem('Velocidade resetada para 100%');
    }

    aplicarVelocidadeGlobal() {
        if (window.jogo) {
            // Aplicar velocidade global em todos os sistemas (EXCETO CENÁRIO)
            if (window.jogo.obstaculoController) {
                const velocidadeBaseObstaculos = 0.18;
                window.jogo.obstaculoController.atualizarVelocidade(velocidadeBaseObstaculos * this.velocidadeGlobal);
            }

            if (window.jogo.personagemController) {
                window.jogo.personagemController.atualizarVelocidadeGlobal(this.velocidadeGlobal);
            }

            // NOVO: Aplicar também no timer se estiver ativo
            if (window.jogo.timerController && window.jogo.timerController.timerAtivo) {
                window.jogo.timerController.aplicarFatorVelocidade(this.velocidadeGlobal);
            }

            console.log(`🎯 Velocidade global aplicada: ${this.velocidadeGlobal.toFixed(2)}`);
        }
    }

    // NOVO: Método para obter velocidade global
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
            this.resetarVelocidade(); // NOVO: Resetar velocidade também

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