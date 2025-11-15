class Cenario {
    constructor(elementId, velocidade, loopPoint = -144.2) {
        this.element = document.getElementById(elementId);
        this.velocidade = velocidade;
        this.velocidadeAlvo = velocidade;
        this.posicao = 0;
        this.loopPoint = loopPoint;
        this.animacaoId = null;
        this.ultimoTempo = 0;
        this.pausado = false;
        
        // NOVO: Configuração para responsividade
        this.resolutionController = window.jogo?.resolutionController;
    }

    iniciarAnimacao() {
        this.ultimoTempo = performance.now();
        this.animar();
    }

    animar(tempoAtual = this.ultimoTempo) {
        if (this.pausado) return;

        const deltaTime = tempoAtual - this.ultimoTempo;
        this.ultimoTempo = tempoAtual;

        // NOVO: Aplicar fator de velocidade baseado na escala
        const fatorVelocidade = this.resolutionController?.getFatorVelocidade() || 1;
        const velocidadeAjustada = this.velocidade * fatorVelocidade;

        // Suavizar transição de velocidade
        if (Math.abs(this.velocidade - this.velocidadeAlvo) > 0.001) {
            this.velocidade += (this.velocidadeAlvo - this.velocidade) * 0.05;
        }

        // Atualizar posição com velocidade ajustada
        this.posicao -= velocidadeAjustada * (deltaTime / 16);
        
        // CORREÇÃO DO LOOP: Usar porcentagem relativa para funcionar em qualquer escala
        const loopPointAjustado = this.loopPoint * (this.resolutionController?.getScale() || 1);
        
        if (this.posicao <= loopPointAjustado) {
            this.posicao = 0;
        }

        // Aplicar background-position em porcentagem (já é relativo)
        this.element.style.backgroundPosition = `${this.posicao}% bottom`;

        this.animacaoId = requestAnimationFrame((tempo) => this.animar(tempo));
    }

    setVelocidade(novaVelocidade) {
        this.velocidadeAlvo = novaVelocidade;
    }

    setVelocidadeSuave(novaVelocidade, duracao = 1000) {
        this.velocidadeAlvo = novaVelocidade;
    }

    pausar() {
        this.pausado = true;
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
        }
    }

    despausar() {
        if (this.pausado) {
            this.pausado = false;
            this.ultimoTempo = performance.now();
            this.animar();
        }
    }

    // NOVO: Método para atualizar com mudanças de resolução
    atualizarParaNovaResolucao() {
        this.resolutionController = window.jogo?.resolutionController;
        // Reiniciar animação para aplicar novas configurações
        if (!this.pausado) {
            this.pausar();
            this.despausar();
        }
    }
}