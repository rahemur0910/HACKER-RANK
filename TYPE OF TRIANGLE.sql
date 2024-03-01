SELECT 
    CASE 
        WHEN A + B <= C OR A + C <= B OR B + C <= A THEN 'Not A Triangle'
        WHEN A = B AND B = C THEN 'Equilateral'
        WHEN A = B OR A = C OR B = C THEN 'Isosceles'
        ELSE 'Scalene'
    END AS Triangle_Type
FROM TRIANGLES;

-- Explanation:
-- The first condition checks if the given side lengths can form a triangle. If the sum of the lengths of any two sides is less than or equal to the length of the third side, it's not a triangle.
-- The next condition checks if all three sides are equal. If so, it's an equilateral triangle.
-- Then, it checks if at least two sides are equal. If so, it's an isosceles triangle.
-- If none of the above conditions are met, it's a scalene triangle.