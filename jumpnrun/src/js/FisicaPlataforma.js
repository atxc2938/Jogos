class FisicaPlataforma {
    constructor() {
        this.alturaChao = 130;
        this.gravidade = 0.6;
    }

    verificarColisaoPlataforma(personagem, plataforma) {
        const px = parseInt(personagem.style.left) || 250;
        const py = parseInt(personagem.style.bottom) || this.alturaChao;
        const pw = 83;
        const ph = 90;

        const ox = plataforma.x;
        const oy = plataforma.y;
        const ow = plataforma.width;
        const oh = plataforma.height;

        const colisaoPorCima = this.verificarColisaoPorCima(py, ph, oy, oh, px, pw, ox, ow);
        
        return {
            porCima: colisaoPorCima,
            plataforma: plataforma
        };
    }

    verificarColisaoPorCima(personagemY, personagemHeight, plataformaY, plataformaHeight, personagemX, personagemWidth, plataformaX, plataformaWidth) {
        const personagemBase = personagemY;
        const personagemTopo = personagemY + personagemHeight;
        const plataformaBase = plataformaY;
        const plataformaTopo = plataformaY + plataformaHeight;
        
        const estaDentroDaPlataformaVertical = personagemBase <= plataformaTopo && personagemBase >= plataformaY;
    
        const colisaoHorizontal = personagemX + personagemWidth > plataformaX + 10 && 
                                 personagemX < plataformaX + plataformaWidth - 10;
        
        const personagemController = window.jogo?.personagemController;
        const estaCaindo = personagemController ? personagemController.velocidadeY <= 2 : true;
        
        const distanciaDoTopo = Math.abs(personagemBase - plataformaTopo);
        const estaProximoDoTopo = distanciaDoTopo < 25;
        
        return estaProximoDoTopo && colisaoHorizontal && estaCaindo && estaDentroDaPlataformaVertical;
    }

    calcularVelocidadeQueda(velocidadeYAtual) {
        return Math.max(-20, velocidadeYAtual - this.gravidade);
    }
}