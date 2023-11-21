import { Alert, AlertIcon, Box, Button, FormLabel, Input, Stack } from '@chakra-ui/react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { errorNotification, successNotification } from '../_services/notification';
import { updateCustomer } from '../_services/client';

const MyTextInput = ({ label, ...props }) => {
	const [field, meta] = useField(props);
	return (
		<Box>
			<FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
			<Input className="text-input" {...field} {...props} />
			{meta.touched && meta.error ? (
				<Alert className="error" status="error" mt={2}>
					<AlertIcon />
					{meta.error}
				</Alert>
			) : null}
		</Box>
	);
};

const EditCustomerForm = ({ id, name, email, age, onClose, fetchCustomers }) => {
	return (
		<>
			<Formik
				initialValues={{
					name: name,
					email: email,
					age: age
				}}
				validationSchema={Yup.object({
					name: Yup.string()
						.max(15, 'Must be 15 characters or less')
						.required('Required'),
					email: Yup.string()
						.email('Invalid email address')
						.required('Required'),
					age: Yup.number()
						.min(16, 'Must be at least 16 years of age')
						.max(99, 'Must be less than 99 years of age')
						.required('Required')
				})}
				onSubmit={(customer, { setSubmitting }) => {
					updateCustomer(id, customer)
						.then(res => {
							console.log(res)
							successNotification(
								"Customer updated",
								`${customer.name} was successfully updated`
							)
							fetchCustomers();
							onClose()
						}).catch(err => {
							console.log(err)
							errorNotification(
								err.code,
								err.response.data.message
							)
						}).finally(() => {
							setSubmitting(false)
						})
				}}
			>
				{({ isSubmitting, isValid, dirty }) => (
					<Form>
						<Stack spacing={4}>
							<MyTextInput
								label="Name"
								name="name"
								type="text"
								placeholder="Jane"
							/>
							<MyTextInput
								label="Email Address"
								name="email"
								type="email"
								placeholder="jane@formik.com"
							/>
							<MyTextInput
								label="Age"
								name="age"
								type="number"
								placeholder="24"
							/>
							<Button
								type="submit"
								isDisabled={!(isValid && dirty)}
								isLoading={isSubmitting}
							>Submit</Button>
						</Stack>
					</Form>
				)}
			</Formik>
		</>
	);
};

export default EditCustomerForm