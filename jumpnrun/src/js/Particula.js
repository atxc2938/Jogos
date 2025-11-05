class Particula {
    constructor() {
        this.container = document.getElementById('particles-container');
        this.particles = [];
        this.lastEmissionTime = 0;
        this.emissionRate = 80;
        this.isActive = false;
    }

    start() {
        this.isActive = true;
        this.animate();
    }

    stop() {
        this.isActive = false;
        this.particles = [];
        this.container.innerHTML = '';
    }

    emitParticles(personagemX) {
        if (!this.isActive) return;

        const currentTime = Date.now();
        if (currentTime - this.lastEmissionTime < this.emissionRate) return;

        this.lastEmissionTime = currentTime;

        const particleCount = Math.floor(Math.random() * 2) + 1;

        for (let i = 0; i < particleCount; i++) {
            this.createParticle(personagemX);
        }
    }

    createParticle(personagemX) {
        const particle = document.createElement('div');
        const particleType = Math.floor(Math.random() * 4) + 1;
        particle.className = `particle type${particleType}`;

        const offsetX = personagemX - 10;
        const offsetY = 135 + (Math.random() * 5);

        const size = Math.random() * 4 + 8;
        const life = Math.random() * 1000 + 600;

        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;
        particle.style.left = `${offsetX}px`;
        particle.style.bottom = `${offsetY}px`;
        particle.style.opacity = '0.8';

        this.container.appendChild(particle);

        const particleData = {
            element: particle,
            startX: offsetX,
            startY: offsetY,
            velocityX: (Math.random() - 0.9) * 6,
            velocityY: Math.random() * 2 + 0.5,
            life: life,
            age: 0,
            size: size
        };

        this.particles.push(particleData);
    }

    animate() {
        if (!this.isActive) return;

        const currentTime = Date.now();
        const deltaTime = currentTime - (this.lastUpdateTime || currentTime);
        this.lastUpdateTime = currentTime;

        for (let i = this.particles.length - 1; i >= 0; i--) {
            const particle = this.particles[i];
            particle.age += deltaTime;

            if (particle.age >= particle.life) {
                particle.element.remove();
                this.particles.splice(i, 1);
                continue;
            }

            const lifeProgress = particle.age / particle.life;

            const newX = particle.startX + (particle.velocityX * particle.age * 0.03);
            const newY = particle.startY - (particle.velocityY * particle.age * 0.01);

            particle.element.style.left = `${newX}px`;
            particle.element.style.bottom = `${newY}px`;

            particle.element.style.opacity = `${0.8 * (1 - lifeProgress)}`;

            const scale = 0.4 + (0.6 * (1 - lifeProgress));
            particle.element.style.transform = `scale(${scale})`;
        }

        requestAnimationFrame(() => this.animate());
    }
}