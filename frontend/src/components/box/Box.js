import { Input } from '../input/Input'
import { History } from '../history/History'
import { useState } from 'react'
import './Box.css'

export const Box = (props) => {
    const [history, handleHistory] = useState([])
    
    const addCommandHistory = (res, isCommand) => {
        const k = history.length.toString()
        const pastCommand = (
            <History 
                key={k} 
                className="history"
                isCommand={isCommand}
                text={res}/>
            )
        
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