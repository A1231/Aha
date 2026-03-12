import React from 'react';

import { render, screen } from '@testing-library/react';

import Button from '../components/Button';

import {describe, it, expect} from 'vitest'

describe('Button', () => {
    it('should render the button', () => {
        render(<Button type="button">Click me</Button>);
        expect(screen.getByText('Click me')).toBeDefined();
    });
    it('should render the button with the correct class', () => {
        render(<Button type="button" variant="primary">Click me</Button>);
        expect(screen.getByText('Click me').classList.contains('btn--primary')).toBe(true);
    });
    it('should render the button with the correct size', () => {
        render(<Button type="button" size="lg">Click me</Button>);
        expect(screen.getByText('Click me').classList.contains('btn--lg')).toBe(true);
    });
    it('should render the button with the correct fullWidth', () => {
        render(<Button type="button" fullWidth>Click me</Button>);
        expect(screen.getByText('Click me').classList.contains('btn--full')).toBe(true);
    });
});