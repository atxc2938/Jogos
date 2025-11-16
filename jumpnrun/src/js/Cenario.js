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
    }

    iniciarAnimacao() {
        this.ultimoTempo = performance.now();
        this.animar();
    }

    animar(tempoAtual = this.ultimoTempo) {
        if (this.pausado) return;

        const deltaTime = tempoAtual - this.ultimoTempo;
        this.ultimoTempo = tempoAtual;

        // Suavizar transição de velocidade
        if (Math.abs(this.velocidade - this.velocidadeAlvo) > 0.001) {
            this.velocidade += (this.velocidadeAlvo - this.velocidade) * 0.05;
        }

        // Atualizar posição
        this.posicao -= this.velocidade * (deltaTime / 16);
        
        if (this.posicao <= this.loopPoint) {
            this.posicao = 0;
        }

        // Aplicar background-position em porcentagem
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
}