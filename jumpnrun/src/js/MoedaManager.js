class MoedaManager {
    constructor() {
        this.moedasTotais = 0;
        this.moedasFaseAtual = 0;
        this.carregarMoedas();
    }

    carregarMoedas() {
        const moedasSalvas = localStorage.getItem('moedasTotais');
        if (moedasSalvas) {
            this.moedasTotais = parseInt(moedasSalvas);
        } else {
            this.moedasTotais = 0;
            this.salvarMoedas();
        }
    }

    salvarMoedas() {
        localStorage.setItem('moedasTotais', this.moedasTotais.toString());
    }

    adicionarMoeda() {
        this.moedasTotais++;
        this.moedasFaseAtual++;
        this.salvarMoedas();
        
        this.dispararEventoAtualizacao();
        return this.moedasFaseAtual;
    }

    removerMoedas(quantidade) {
        if (this.moedasTotais >= quantidade) {
            this.moedasTotais -= quantidade;
            this.salvarMoedas();
            this.dispararEventoAtualizacao();
            return true;
        }
        return false;
    }

    getMoedasTotais() {
        return this.moedasTotais;
    }

    getMoedasFaseAtual() {
        return this.moedasFaseAtual;
    }

    reiniciarFase() {
        this.moedasFaseAtual = 0;
    }

    resetarTudo() {
        this.moedasTotais = 0;
        this.moedasFaseAtual = 0;
        this.salvarMoedas();
        this.dispararEventoAtualizacao();
    }

    dispararEventoAtualizacao() {
        // Disparar evento customizado para notificar outras partes do sistema
        const evento = new CustomEvent('moedasAtualizadas', {
            detail: { moedasTotais: this.moedasTotais }
        });
        window.dispatchEvent(evento);
    }
}