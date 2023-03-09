import { useState, useEffect } from 'react'
import axios from 'axios'
import './Input.css'

export const Input = (props) => {
    const [command, handleCommand] = useState('')
    const [commandResponse, handleCommandResponse] = useState({})
    const [triggerCommand, toggleTriggerCommand] = useState(false)
    const [triggerCommandResponse, toggleTriggerCommandResponse] = useState(false)

    const [disabled, toggleDisabled] = useState(false)


    const handleSubmit = (e) => {
        e.preventDefault()
        
        axios({
            method: 'post',
            url: `http://${process.env.REACT_APP_BACKEND}:4567/`,
            params: {
                'token': props.sessionToken
            },
            data: {
              command: command
            },
            withCredentials: true
        })
        .then((res) => res.data)
        .then((json) => handleCommandResponse(json))
        .then(() => toggleTriggerCommand(true))
        .catch((err) => {
            toggleDisabled(true)
            alert("There was a system error, please refresh your page")
        })
    }

    /* Waterfall Effect */
    useEffect(() => {
        if (triggerCommand) {
            props.onSubmitCommand(command)
            handleCommand('')
            
            toggleTriggerCommandResponse(true)
        }
    }, [triggerCommand])

    useEffect(() => {
        if (triggerCommandResponse) {
            props.onSubmitCommandResponse(commandResponse)

            toggleTriggerCommand(false)
            toggleTriggerCommandResponse(false)
        }
    }, [triggerCommandResponse])
    /* ----------------- */


    return (
        <div>
            <p>{">> "}</p>
            <form onSubmit={handleSubmit} autoComplete="off" spellCheck="false">
                <input
                    id="commandInput"
                    type="input"
                    value={command}
                    disabled={disabled}
                    onChange={(e) => handleCommand(e.target.value)}
                    autoFocus
                />
                <br/>
            </form>
        </div>
    )
}
