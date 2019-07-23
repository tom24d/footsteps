function f(debt, term, interest) {
    return debt*(interest/12)/(1-Math.pow(1+interest/12, -term*12))
}
function exe() {
    for (let i = 30000000; i < 50000000; i += 1000000) {
        f(i, 30, 0.02)
    }
}

console.time('test');

exe();

console.timeEnd('test');
