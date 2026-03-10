
import type { Question } from "../types/Question";

function AddQuestion({ question, index, setQuestions, questions }: { question: Question, index: number, setQuestions: (questions: Question[]) => void, questions: Question[] }) {

    return (

        <div className="question-container">

            <label htmlFor="question">Question</label>
            <input type="text" id="question" name="question" value={question.text} 
            onChange={(e) => 
            setQuestions(questions.map((question, i) => i === index ? { ...question, text: e.target.value } : question))} 
            required />
            <label htmlFor="options">Options</label>
            {Array.from({length: 4}, (_, idx) => (
                <div key={idx}>

                <label htmlFor={`option${idx + 1}`}>Option {idx + 1}</label>
                <input  type="text" id={`option${idx + 1}`} name={`option${idx + 1}`} value={question.options[idx]} 
                onChange={(e) => 
                setQuestions(questions.map((question, i) => i === index ? { ...question, options: question.options.map((option, j) => j === idx ? e.target.value : option) } : question))} required />
                </div>
            ))}
            <label htmlFor="correctOption">Correct Option</label>
            <select id="correctOption" name="correctOption" value={question.correctOptionIndex} 
            onChange={(e) => setQuestions(questions.map((question, i) => i === index ? { ...question, correctOptionIndex: parseInt(e.target.value) } : question))} required>
                {Array.from({length: 4}, (_, index) => (
                    <option key={index} value={index}>Option {index + 1}</option>
                ))}
            </select>
        </div>
    )
    

   
}

export default AddQuestion;