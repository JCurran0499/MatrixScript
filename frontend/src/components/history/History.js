import './History.css'

export const History = (props) => {
    const getText = () => props.isCommand ? (">> " + props.text) : (props.text)

    return (
        <div>
            <p>{getText()}</p>
            {!props.isCommand && (
                <div>
                    <br/>
                </div>
            )}
        </div>
    )
}