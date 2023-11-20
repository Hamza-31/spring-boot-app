import {
	Button,
	Drawer,
	DrawerBody,
	DrawerCloseButton,
	DrawerContent,
	DrawerFooter,
	DrawerHeader,
	DrawerOverlay,
	useDisclosure
} from '@chakra-ui/react'
import CreateCustomerForm from './CreateCustomerForm';

const AddIcon = () => "+";
const CloseIcon = () => "x";
const DrawerForm = ({ fetchCustomers }) => {
	const { isOpen, onOpen, onClose } = useDisclosure()

	return (
		<>
			<Button
				colorScheme={"teal"}
				onClick={onOpen}
				leftIcon={<AddIcon />}
			>Create customer</Button>
			<Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
				<DrawerOverlay />
				<DrawerContent>
					<DrawerCloseButton />
					<DrawerHeader>Create new Customer</DrawerHeader>

					<DrawerBody>
						<CreateCustomerForm fetchCustomers={fetchCustomers} />
					</DrawerBody>

					<DrawerFooter>
						<Button
							colorScheme={"teal"}
							onClick={onClose}
							leftIcon={<CloseIcon />}
						>
							Close
						</Button>
					</DrawerFooter>
				</DrawerContent>
			</Drawer>
		</>
	)
}

export default DrawerForm

