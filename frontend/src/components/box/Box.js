import { Input } from '../input/Input'
import { History } from '../history/History'
import { useState, useEffect } from 'react'
import './Box.css'

export const Box = (props) => {
    const [history, handleHistory] = useState([])

    const commandInput = document.getElementById("commandInput")
    const focusInput = () => {
        commandInput.focus()
    }

    const updateScroll = () => {
        const box = document.getElementById("box");
        box.scrollTop = box.scrollHeight;
    }

    const clearBox = (e) => {
        handleHistory([])
        focusInput()
    }
    
    const addCommandHistory = (c) => {
        const k = history.length
        const pastCommand = (
            <History 
                key={k} 
                className="history"
                isCommand={true}
                isMatrix={false}
                text={c}/>
            )
        
        handleHistory([...history, pastCommand])
    }

    const addCommandResponseHistory = (cr) => {
        let c
        let m
        if (cr.response.matrix) {
            c = cr.response.matrix
            m = true
        }
        else {
            c = cr.response
            m = false
        }

        const k = history.length
        const pastCommand = (
            <History 
                key={k} 
                className="history"
                isCommand={false}
                isMatrix={m}
                text={c}/>
            )
        
        handleHistory([...history, pastCommand])
    }

    useEffect(updateScroll, [history])


    return (
        <div>
            <div id="box" onClick={focusInput}>
                {history}
                <Input
                    id="input"
                    sessionToken={props.sessionToken}
                    onSubmitCommand={addCommandHistory}
                    onSubmitCommandResponse={addCommandResponseHistory}
                />
            </div>
            <button 
                onClick={clearBox}
            >CLEAR</button>
        </div>
    )
}