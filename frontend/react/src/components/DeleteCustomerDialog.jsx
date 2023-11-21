import { AlertDialog, AlertDialogBody, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogOverlay, Button, useDisclosure } from "@chakra-ui/react"
import React from "react"
import { deleteCustomer } from "../_services/client"
import { errorNotification, successNotification } from "../_services/notification"

function DeleteCustomerDialog({ id, name, fetchCustomers }) {
	const { isOpen, onOpen, onClose } = useDisclosure()
	const cancelRef = React.useRef()

	const onDelete = (id) => {
		deleteCustomer(id)
			.then(res => {
				console.log(res)
				successNotification(
					"Customer deleted",
					`Customer "${name}" was successfully deleted`
				)
				onClose()
				fetchCustomers();
			}).catch(err => {
				console.log(err)
				errorNotification(
					err.code,
					err.response.data.message
				)
			}).finally(() => {
				onClose()
			})
	}
	return (
		<>
			<Button
				colorScheme='red'
				variant="outline"
				onClick={onOpen}
			>
				Delete
			</Button>

			<AlertDialog
				isOpen={isOpen}
				leastDestructiveRef={cancelRef}
				onClose={onClose}
			>
				<AlertDialogOverlay>
					<AlertDialogContent>
						<AlertDialogHeader fontSize='lg' fontWeight='bold'>
							Delete Customer &quot;{name}&quot;
						</AlertDialogHeader>

						<AlertDialogBody>
							Are you sure? You can&apos;t undo this action afterwards.
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button ref={cancelRef} onClick={onClose}>
								Cancel
							</Button>
							<Button colorScheme='red' onClick={() => onDelete(id)} ml={3}>
								Delete
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</>
	)
}

export default DeleteCustomerDialog;