import { ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  // className은 이미 ButtonHTMLAttributes에 포함되어 있어 생략 가능하지만, 
  // 명시적으로 적어주는 것도 나쁘지 않습니다.
  className?: string;
}

const Button = ({ className, children, type = "button", ...rest }: ButtonProps) => {
  return (
    <button className={className} type={type} {...rest}>
      {children}
    </button>
  );
};

export default Button;