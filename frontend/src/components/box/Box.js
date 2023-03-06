import { Input } from '../input/Input'
import { useState } from 'react'
import './Box.css'

export const Box = (props) => {
    const [history, handleHistory] = useState([])
    
    const addCommandHistory = (res) => {
        const k = history.length.toString()
        const pastCommand = <p key={k} className="history">{res}</p>
        
        handleHistory([...history, pastCommand])
    }


    return (
        <div>
            <div id="outputbox">
                {history}
                <Input
                    id="input"
                    sessionToken={props.sessionToken}
                    onSubmit={addCommandHistory}
                />
            </div>
            <button 
                onClick={(e) => handleHistory([])}
            >RESET</button>
        </div>
    )
}