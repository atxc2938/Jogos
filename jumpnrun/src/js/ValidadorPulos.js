class ValidadorPulos {
    constructor() {
        this.velocidadeBaseObstaculos = 0.18;
        this.velocidadePuloBase = 18;
        this.gravidadeBase = 0.6;
        this.velocidadePulo = this.velocidadePuloBase;
        this.gravidade = this.gravidadeBase;
        this.margemSeguranca = 1.3;
        this.alturaMaximaPulo = this.calcularAlturaMaximaPulo();
        this.distanciaMaximaPulo = this.calcularDistanciaMaximaPulo();
        this.fatorFisica = 1;
    }

    atualizarVelocidade(novaVelocidade) {
        // Não atualizamos mais a velocidade base para cálculos
        this.distanciaMaximaPulo = this.calcularDistanciaMaximaPulo();
    }

    atualizarFisicaParaEscala(fatorVelocidade) {
        this.fatorFisica = fatorVelocidade;
        this.velocidadePulo = this.velocidadePuloBase * fatorVelocidade;
        this.gravidade = this.gravidadeBase * fatorVelocidade * fatorVelocidade;
        this.distanciaMaximaPulo = this.calcularDistanciaMaximaPulo();
    }

    calcularAlturaMaximaPulo() {
        return (this.velocidadePulo * this.velocidadePulo) / (2 * this.gravidade);
    }

    calcularDistanciaMaximaPulo() {
        const tempoPulo = (2 * this.velocidadePulo) / this.gravidade;
        // Usamos a velocidade BASE dos obstáculos, não a velocidade atual escalada
        const distanciaPulo = (this.velocidadeBaseObstaculos * 1920 / 100) * tempoPulo;
        return distanciaPulo * this.margemSeguranca;
    }

    validarPadrao(padrao) {
        switch(padrao.tipo) {
            case 'plataforma_escada':
                return this.validarPlataformasEscada(padrao);
            case 'espinho':
                return this.validarEspinhos(padrao);
            case 'misto_escada':
                return this.validarMistoEscada(padrao);
            default:
                return true;
        }
    }

    validarPlataformasEscada(padrao) {
        const { sequenciaAlturas, espacamento } = padrao;
        
        for (let i = 1; i < sequenciaAlturas.length; i++) {
            const alturaAtual = sequenciaAlturas[i];
            const alturaAnterior = sequenciaAlturas[i - 1];
            const diferencaAltura = Math.abs(alturaAtual - alturaAnterior);
            
            if (diferencaAltura > 2) {
                return false;
            }
            
            if (alturaAtual > alturaAnterior && !this.podePularParaAltura(alturaAtual, alturaAnterior, espacamento)) {
                return false;
            }
        }
        
        return true;
    }

    validarEspinhos(padrao) {
        const { quantidade, espacamento } = padrao;
        
        if (espacamento < this.calcularEspacamentoMinimoEspinhos()) {
            return false;
        }
        
        const distanciaTotal = (quantidade - 1) * espacamento;
        let limiteDistancia;
        
        switch(window.jogo?.dificuldadeAtual) {
            case 'facil':
                limiteDistancia = this.distanciaMaximaPulo * 2.0;
                break;
            case 'medio':
                limiteDistancia = this.distanciaMaximaPulo * 1.5;
                break;
            case 'dificil':
                limiteDistancia = this.distanciaMaximaPulo * 1.2;
                break;
            default:
                limiteDistancia = this.distanciaMaximaPulo * 1.5;
        }
        
        if (distanciaTotal > limiteDistancia) {
            return false;
        }
        
        return true;
    }

    validarMistoEscada(padrao) {
        const { sequenciaAlturas, quantidade, espacamento } = padrao;
        
        for (let i = 0; i < sequenciaAlturas.length; i++) {
            if (i > 0) {
                const alturaAtual = sequenciaAlturas[i];
                const alturaAnterior = sequenciaAlturas[i - 1];
                const diferencaAltura = Math.abs(alturaAtual - alturaAnterior);
                
                if (diferencaAltura > 2) {
                    return false;
                }
            }
        }
        
        const espacosEntreObstaculos = quantidade - 1;
        let limiteDistancia;
        
        switch(window.jogo?.dificuldadeAtual) {
            case 'facil':
                limiteDistancia = this.distanciaMaximaPulo * 2.2;
                break;
            case 'medio':
                limiteDistancia = this.distanciaMaximaPulo * 1.8;
                break;
            case 'dificil':
                limiteDistancia = this.distanciaMaximaPulo * 1.5;
                break;
            default:
                limiteDistancia = this.distanciaMaximaPulo * 1.8;
        }
        
        if (espacosEntreObstaculos * espacamento > limiteDistancia) {
            return false;
        }
        
        return true;
    }

    podePularParaAltura(alturaDestino, alturaOrigem, distancia) {
        const diferencaAltura = (alturaDestino - alturaOrigem) * 80;
        const alturaMaximaPossivel = this.alturaMaximaPulo - diferencaAltura;
        
        if (alturaMaximaPossivel < 0) {
            return false;
        }
        
        const distanciaRequerida = this.calcularDistanciaParaAltura(alturaMaximaPossivel);
        return distancia <= distanciaRequerida;
    }

    calcularDistanciaParaAltura(altura) {
        const tempoSubida = Math.sqrt(2 * altura / this.gravidade);
        const distancia = (this.velocidadeBaseObstaculos * 1920 / 100) * tempoSubida * 2;
        return distancia * 0.9;
    }

    calcularEspacamentoPlataformas() {
        const tempoPulo = (2 * this.velocidadePulo) / this.gravidade;
        const distanciaPulo = (this.velocidadeBaseObstaculos * 1920 / 100) * tempoPulo;
        
        const espacamentoMinimo = distanciaPulo * this.margemSeguranca;
        
        return Math.max(140, espacamentoMinimo);
    }

    calcularEspacamentoMinimoEspinhos() {
        const tempoPulo = (2 * this.velocidadePulo) / this.gravidade;
        const distanciaPulo = (this.velocidadeBaseObstaculos * 1920 / 100) * tempoPulo;
        
        let fatorDificuldade;
        switch(window.jogo?.dificuldadeAtual) {
            case 'facil':
                fatorDificuldade = 0.5;
                break;
            case 'medio':
                fatorDificuldade = 0.4;
                break;
            case 'dificil':
                fatorDificuldade = 0.3;
                break;
            default:
                fatorDificuldade = 0.4;
        }
        
        const espacamentoMinimo = (distanciaPulo * this.margemSeguranca) * fatorDificuldade;
        
        return Math.max(180, espacamentoMinimo);
    }
}