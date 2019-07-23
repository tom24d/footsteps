import React, {useState} from 'react';
import logo from './logo.svg';
import './App.css';
import {TextField} from '@material-ui/core';

function App() {

    const [debt, setDebt] = useState(0);
    const [term, setTerm] = useState(0);
    const [interest, setInterest] = useState(0.0);

    const calcer = () => {
        if (debt === 0 || term === 0 || interest == 0) return 0;
        return Math.ceil(debt * (interest / 12) / (1 - Math.pow(1 + interest / 12, -term * 12)));
    };

    function f(debt, term, interest) {
        return debt*(interest/12)/(1-(1+(interest/12)**(-term*12)))
    }

    function exe() {
        for (let i = 30000000; i < 50000000; i += 1000000) {
            f(i, 30, 0.02)
        }
    }

    console.time('test');

    exe();

    console.timeEnd('test');

    return (
        <div className="App">
            <header className="App-header">
                <h5>元利均等返済 シミュレータ</h5>
                <TextField required value={debt} label="Debt"
                           onChange={(e) => setDebt(e.target.value)}/>
                <TextField required value={term} label="Term"
                           onChange={(e) => setTerm(e.target.value)}/>
                <TextField required value={interest} label="Interest"
                           onChange={(e) => setInterest(e.target.value)}/>
                <p>毎月の返済額:{calcer().toLocaleString()}円</p>
                <h6>made by Tomoya Nishide</h6>
            </header>
        </div>
    );
}

export default App;
