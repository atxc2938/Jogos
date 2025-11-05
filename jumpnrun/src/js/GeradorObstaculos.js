class GeradorObstaculos {
    constructor() {
        this.velocidadeAtual = 0.18;
        this.dificuldade = 'medio';
        this.ultimoPadrao = null;
        this.contadorPadroes = 0;
        this.alturaBasePlataforma = 134;
        this.validador = new ValidadorPulos();
    }

    atualizarVelocidade(novaVelocidade) {
        this.velocidadeAtual = novaVelocidade;
        this.validador.atualizarVelocidade(novaVelocidade);
    }

    atualizarDificuldade(novaDificuldade) {
        this.dificuldade = novaDificuldade;
    }

    gerarProximoPadrao() {
        this.contadorPadroes++;
        
        const complexidade = Math.min(5, Math.floor(this.contadorPadroes / 8) + 1);
        const chanceComplexo = Math.min(0.9, (this.velocidadeAtual - 0.18) / 0.4);
        
        const tipos = ['plataforma', 'espinho', 'misto'];
        const pesos = this.calcularPesos(complexidade, chanceComplexo);
        
        const tipo = this.escolherComPeso(tipos, pesos);
        
        let padrao;
        let tentativas = 0;
        const maxTentativas = 5;
        
        do {
            switch(tipo) {
                case 'plataforma':
                    padrao = this.gerarPadraoPlataformaEscalonada(complexidade);
                    break;
                case 'espinho':
                    padrao = this.gerarPadraoEspinhoSuave(complexidade);
                    break;
                case 'misto':
                    padrao = this.gerarPadraoMistoEscalonado(complexidade);
                    break;
            }
            tentativas++;
        } while (!this.validador.validarPadrao(padrao) && tentativas < maxTentativas);
        
        return padrao;
    }

    calcularPesos(complexidade, chanceComplexo) {
        let basePlataforma, baseEspinho, baseMisto;
        
        switch(this.dificuldade) {
            case 'facil':
                basePlataforma = 0.8;
                baseEspinho = 0.1;
                baseMisto = 0.1;
                break;
            case 'medio':
                basePlataforma = 0.6;
                baseEspinho = 0.2;
                baseMisto = 0.2;
                break;
            case 'dificil':
                basePlataforma = 0.5;
                baseEspinho = 0.25;
                baseMisto = 0.25;
                break;
            default:
                basePlataforma = 0.6;
                baseEspinho = 0.2;
                baseMisto = 0.2;
        }
        
        const ajusteComplexo = complexidade * 0.15;
        
        return [
            basePlataforma - ajusteComplexo,
            baseEspinho + (chanceComplexo * 0.25),
            baseMisto + ajusteComplexo
        ];
    }

    gerarPadraoPlataformaEscalonada(complexidade) {
        let alturaMaxima;
        
        switch(this.dificuldade) {
            case 'facil':
                alturaMaxima = 2;
                break;
            case 'medio':
                alturaMaxima = 3;
                break;
            case 'dificil':
                alturaMaxima = 4;
                break;
            default:
                alturaMaxima = 3;
        }
        
        let alturaAlvo;
        if (this.dificuldade === 'dificil') {
            if (Math.random() < 0.4) {
                alturaAlvo = 4;
            } else if (Math.random() < 0.6) {
                alturaAlvo = 3;
            } else {
                alturaAlvo = 2;
            }
        } else if (this.dificuldade === 'medio') {
            alturaAlvo = Math.random() < 0.2 ? 3 : 2;
        } else {
            alturaAlvo = 1;
        }
        
        alturaAlvo = Math.min(alturaAlvo, alturaMaxima);
        
        const sequenciaAlturas = this.gerarSequenciaAlturas(alturaAlvo);
        const deveTerEspinhos = this.deveAdicionarEspinhos(sequenciaAlturas);
        
        return {
            tipo: 'plataforma_escada',
            sequenciaAlturas: sequenciaAlturas,
            espacamento: this.calcularEspacamentoPlataformas(),
            comEspinhos: deveTerEspinhos
        };
    }

    gerarPadraoEspinhoSuave(complexidade) {
        let quantidadeBase;
        
        if (this.velocidadeAtual < 0.22) {
            quantidadeBase = 2;
        } else if (this.velocidadeAtual < 0.28) {
            quantidadeBase = 3;
        } else if (this.velocidadeAtual < 0.35) {
            quantidadeBase = 4;
        } else {
            quantidadeBase = 5;
        }
        
        if (this.dificuldade === 'facil') {
            quantidadeBase = Math.max(1, quantidadeBase - 1);
        } else if (this.dificuldade === 'dificil') {
            quantidadeBase = Math.min(8, quantidadeBase + 2);
        }
        
        const quantidadeExtra = Math.random() < (complexidade * 0.3) ? 1 : 0;
        const quantidade = Math.min(8, quantidadeBase + quantidadeExtra);
        
        let espacamentoMinimo = this.calcularEspacamentoMinimoEspinhos();
        let espacamentoMaximo;
        
        switch(this.dificuldade) {
            case 'facil':
                espacamentoMinimo *= 1.4;
                espacamentoMaximo = espacamentoMinimo * 2.2;
                break;
            case 'medio':
                espacamentoMinimo *= 1.1;
                espacamentoMaximo = espacamentoMinimo * 1.8;
                break;
            case 'dificil':
                espacamentoMinimo *= 0.6;
                espacamentoMaximo = espacamentoMinimo * 1.2;
                break;
            default:
                espacamentoMinimo *= 1.1;
                espacamentoMaximo = espacamentoMinimo * 1.8;
        }
        
        const espacamento = espacamentoMinimo + Math.random() * (espacamentoMaximo - espacamentoMinimo);
        
        return {
            tipo: 'espinho',
            quantidade: quantidade,
            espacamento: espacamento
        };
    }

    gerarPadraoMistoEscalonado(complexidade) {
        let alturaAlvo;
        
        if (this.dificuldade === 'dificil') {
            alturaAlvo = Math.random() < 0.5 ? 3 : 2;
        } else if (this.dificuldade === 'medio') {
            alturaAlvo = Math.random() < 0.3 ? 3 : 2;
        } else {
            alturaAlvo = 1;
        }
        
        const sequenciaAlturas = this.gerarSequenciaAlturas(alturaAlvo);
        const quantidade = Math.min(6, sequenciaAlturas.length * 2);
        const deveTerEspinhos = this.dificuldade !== 'facil' && this.deveAdicionarEspinhos(sequenciaAlturas);
        
        return {
            tipo: 'misto_escada',
            sequenciaAlturas: sequenciaAlturas,
            quantidade: quantidade,
            espacamento: this.calcularEspacamentoPlataformas(),
            comEspinhos: deveTerEspinhos
        };
    }

    deveAdicionarEspinhos(sequenciaAlturas) {
        if (sequenciaAlturas.length < 2) return false;
        
        for (let i = 0; i < sequenciaAlturas.length; i++) {
            if (sequenciaAlturas[i] > 1) {
                return true;
            }
        }
        
        return false;
    }

    gerarSequenciaAlturas(alturaAlvo) {
        const sequencia = [];
        
        for (let altura = 1; altura <= alturaAlvo; altura++) {
            sequencia.push(altura);
        }
        
        const chanceDescida = this.dificuldade === 'dificil' ? 0.6 : 
                            this.dificuldade === 'medio' ? 0.3 : 0.1;
        
        if (alturaAlvo > 1 && Math.random() < chanceDescida) {
            for (let altura = alturaAlvo - 1; altura >= 1; altura--) {
                sequencia.push(altura);
            }
        }
        
        return sequencia;
    }

    calcularEspacamentoPlataformas() {
        return this.validador.calcularEspacamentoPlataformas();
    }

    calcularEspacamentoMinimoEspinhos() {
        return this.validador.calcularEspacamentoMinimoEspinhos();
    }

    calcularYParaAltura(altura) {
        return this.alturaBasePlataforma + ((altura - 1) * 80);
    }

    escolherComPeso(opcoes, pesos) {
        const total = pesos.reduce((sum, weight) => sum + weight, 0);
        let random = Math.random() * total;
        
        for (let i = 0; i < opcoes.length; i++) {
            random -= pesos[i];
            if (random <= 0) {
                return opcoes[i];
            }
        }
        
        return opcoes[opcoes.length - 1];
    }

    reiniciar() {
        this.contadorPadroes = 0;
        this.ultimoPadrao = null;
    }
}