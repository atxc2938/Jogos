class Cenario {
    constructor(seletor, velocidade, loopPoint) {
        this.elemento = document.getElementById(seletor);
        this.velocidade = velocidade;
        this.velocidadeAlvo = velocidade;
        this.loopPoint = loopPoint;
        this.posicao = 0;
        this.animacaoId = null;
        this.transicaoVelocidade = null;
        this.pausado = false;
        this.fatorVelocidade = 1.0;
    }

    iniciarAnimacao() {
        const animar = () => {
            if (!this.pausado) {
                const velocidadeEfetiva = this.velocidade * this.fatorVelocidade;
                this.posicao -= velocidadeEfetiva;
                
                if (this.loopPoint !== undefined && this.posicao <= this.loopPoint) {
                    this.posicao = 0;
                }
                
                this.elemento.style.backgroundPosition = `${this.posicao}% bottom`;
            }
            
            this.animacaoId = requestAnimationFrame(animar);
        };
        
        this.animacaoId = requestAnimationFrame(animar);
    }

    setVelocidade(novaVelocidade) {
        this.velocidade = novaVelocidade;
        this.velocidadeAlvo = novaVelocidade;
    }

    setVelocidadeSuave(novaVelocidade, duracao) {
        this.velocidadeAlvo = novaVelocidade;
        
        if (this.transicaoVelocidade) {
            clearInterval(this.transicaoVelocidade);
        }
        
        const velocidadeInicial = this.velocidade;
        const diferenca = novaVelocidade - velocidadeInicial;
        const startTime = Date.now();
        
        const transicionar = () => {
            const tempoAtual = Date.now();
            const progresso = Math.min((tempoAtual - startTime) / duracao, 1);
            const progressoSuavizado = progresso * progresso * (3 - 2 * progresso);
            
            this.velocidade = velocidadeInicial + (diferenca * progressoSuavizado);
            
            if (progresso >= 1) {
                clearInterval(this.transicaoVelocidade);
                this.transicaoVelocidade = null;
            }
        };
        
        this.transicaoVelocidade = setInterval(transicionar, 16);
    }

    atualizarFatorVelocidade(fator) {
        this.fatorVelocidade = fator;
    }

    pausar() {
        this.pausado = true;
    }

    despausar() {
        this.pausado = false;
    }

    pararAnimacao() {
        if (this.animacaoId) {
            cancelAnimationFrame(this.animacaoId);
        }
        if (this.transicaoVelocidade) {
            clearInterval(this.transicaoVelocidade);
        }
    }

    setLoopPoint(novoLoopPoint) {
        this.loopPoint = novoLoopPoint;
    }
}