
import { ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  className?: string;
}

const Button = (props: ButtonProps) => {
  const { ...rest } = props;
  return (
    <button {...rest}>
      {props.children}
    </button>
  );
};

export default Button;