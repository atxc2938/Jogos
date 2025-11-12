class SkinManager {
    constructor() {
        this.skins = [];
        this.skinAtual = 'personagem_principal';
        //ADICIONAR SKINS
        this.arquivosReais = [
            'personagem_principal.png',
            'oshawott.png', 
            'bisteca.png',
            'Sério.png'
        ];
        this.precosSkins = {
            'personagem_principal': 0,
            'oshawott': 15,
            'bisteca': 10,
            'Sério': 5
        };
        this.inicializarSkins();
    }

    inicializarSkins() {
        this.carregarSkinsDinamicamente();
        this.carregarProgresso();
        setTimeout(() => this.aplicarSkinNoPersonagem(), 100);
    }

    carregarSkinsDinamicamente() {
        this.skins = this.arquivosReais.map((arquivo, index) => {
            const nomeSkin = this.formatarNomeSkin(arquivo);
            const id = arquivo.replace(/\.[^/.]+$/, "");
            const preco = this.precosSkins[id] || 1;
            const isPrimeira = index === 0;
            
            return {
                id: id,
                nome: nomeSkin,
                imagem: `../assets/images/personagens/${arquivo}`,
                preco: preco,
                comprado: isPrimeira,
                arquivo: arquivo
            };
        });
    }

    sincronizarSkins() {
        const skinsParaManter = this.skins.filter(skin => this.arquivosReais.includes(skin.arquivo));
        this.arquivosReais.forEach(arquivo => {
            const skinExistente = skinsParaManter.find(s => s.arquivo === arquivo);
            if (!skinExistente) {
                skinsParaManter.push(this.criarSkin(arquivo));
            }
        });
        this.skins = skinsParaManter;
    }

    criarSkin(arquivo) {
        const nomeSkin = this.formatarNomeSkin(arquivo);
        const id = arquivo.replace(/\.[^/.]+$/, "");
        const preco = this.precosSkins[id] || 1;
        
        return {
            id: id,
            nome: nomeSkin,
            imagem: `../assets/images/personagens/${arquivo}`,
            preco: preco,
            comprado: false,
            arquivo: arquivo
        };
    }

    verificarNovasSkins() {
        this.sincronizarSkins();
        const skinAtualExiste = this.skins.find(s => s.id === this.skinAtual);
        if (!skinAtualExiste) {
            this.skinAtual = 'personagem_principal';
            this.salvarProgresso();
            this.aplicarSkinNoPersonagem();
        }
        return true;
    }

    formatarNomeSkin(nomeArquivo) {
        let nomeSemExtensao = nomeArquivo.replace(/\.[^/.]+$/, "");
        nomeSemExtensao = nomeSemExtensao.replace(/[_-]/g, ' ');
        return nomeSemExtensao.replace(/\b\w/g, l => l.toUpperCase());
    }

    carregarProgresso() {
        const progressoSalvo = localStorage.getItem('progressoSkins');
        if (progressoSalvo) {
            try {
                const progresso = JSON.parse(progressoSalvo);
                this.sincronizarSkins();
                this.skins = this.skins.map(skin => {
                    const skinSalva = progresso.skins.find(s => s.id === skin.id);
                    if (skinSalva) {
                        return { ...skin, comprado: skinSalva.comprado };
                    }
                    return skin;
                });
                const skinAtualExiste = this.skins.find(s => s.id === progresso.skinAtual);
                this.skinAtual = skinAtualExiste ? progresso.skinAtual : 'personagem_principal';
            } catch (error) {
                this.skinAtual = 'personagem_principal';
            }
        }
        this.salvarProgresso();
    }

    salvarProgresso() {
        const progresso = {
            skins: this.skins,
            skinAtual: this.skinAtual
        };
        localStorage.setItem('progressoSkins', JSON.stringify(progresso));
    }

    comprarSkin(skinId, moedaManager) {
        const skin = this.skins.find(s => s.id === skinId);
        if (!skin || skin.comprado) return false;

        if (moedaManager.getMoedasTotais() >= skin.preco) {
            const sucesso = moedaManager.removerMoedas(skin.preco);
            if (sucesso) {
                skin.comprado = true;
                this.salvarProgresso();
                return true;
            }
        }
        return false;
    }

    selecionarSkin(skinId) {
        const skin = this.skins.find(s => s.id === skinId);
        if (skin && skin.comprado) {
            this.skinAtual = skinId;
            this.salvarProgresso();
            this.aplicarSkinNoPersonagem();
            return true;
        }
        return false;
    }

    aplicarSkinNoPersonagem() {
        const personagem = document.getElementById('personagem');
        if (personagem) {
            const skinAtual = this.getSkinAtual();
            if (skinAtual && skinAtual.imagem) {
                personagem.style.backgroundImage = `url('${skinAtual.imagem}')`;
            } else {
                const skinPadrao = this.skins.find(s => s.id === 'personagem_principal');
                if (skinPadrao) {
                    personagem.style.backgroundImage = `url('${skinPadrao.imagem}')`;
                    this.skinAtual = 'personagem_principal';
                    this.salvarProgresso();
                }
            }
        }
    }

    getSkinAtual() {
        return this.skins.find(s => s.id === this.skinAtual);
    }

    getSkins() {
        return this.skins;
    }

    forcarAtualizacaoCompleta() {
        localStorage.removeItem('progressoSkins');
        this.inicializarSkins();
    }

    resetarProgresso() {
        this.skins.forEach((skin, index) => {
            if (index !== 0) {
                skin.comprado = false;
            }
        });
        this.skinAtual = 'personagem_principal';
        this.salvarProgresso();
        this.aplicarSkinNoPersonagem();
    }
}