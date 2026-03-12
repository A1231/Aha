import type { Question } from "../types/Question";

function AddQuestion({
    question,
    index,
    setQuestions,
    questions,
}: {
    question: Question;
    index: number;
    setQuestions: (questions: Question[]) => void;
    questions: Question[];
}) {
    return (
        <div className="question-builder">
            <span className="question-builder__number">Question {index + 1}</span>

            <div className="form-group">
                <label htmlFor={`question-${index}`}>Question</label>
                <input
                    type="text"
                    id={`question-${index}`}
                    placeholder="Enter your question"
                    value={question.text}
                    onChange={(e) =>
                        setQuestions(
                            questions.map((q, i) =>
                                i === index ? { ...q, text: e.target.value } : q
                            )
                        )
                    }
                    required
                />
            </div>

            <div className="question-builder__options">
                {Array.from({ length: 4 }, (_, idx) => (
                    <div className="form-group" key={idx}>
                        <label htmlFor={`q${index}-option${idx + 1}`}>Option {idx + 1}</label>
                        <input
                            type="text"
                            id={`q${index}-option${idx + 1}`}
                            placeholder={`Option ${idx + 1}`}
                            value={question.options[idx]}
                            onChange={(e) =>
                                setQuestions(
                                    questions.map((q, i) =>
                                        i === index
                                            ? {
                                                  ...q,
                                                  options: q.options.map((opt, j) =>
                                                      j === idx ? e.target.value : opt
                                                  ),
                                              }
                                            : q
                                    )
                                )
                            }
                            required
                        />
                    </div>
                ))}
            </div>

            <div className="form-group mt-2">
                <label htmlFor={`q${index}-correct`}>Correct Option</label>
                <select
                    id={`q${index}-correct`}
                    value={question.correctOptionIndex}
                    onChange={(e) =>
                        setQuestions(
                            questions.map((q, i) =>
                                i === index
                                    ? { ...q, correctOptionIndex: parseInt(e.target.value) }
                                    : q
                            )
                        )
                    }
                    required
                >
                    {Array.from({ length: 4 }, (_, i) => (
                        <option key={i} value={i}>
                            Option {i + 1}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
}

export default AddQuestion;
